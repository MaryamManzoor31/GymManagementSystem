param(
    [string]$MySqlConnectorJar = ".\lib\mysql-connector-j-8.4.0.jar",
    [string]$DatabaseUser = "root"
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

if ([System.IO.Path]::IsPathRooted($MySqlConnectorJar)) {
    $connectorJar = $MySqlConnectorJar
} else {
    $connectorJar = Join-Path $projectRoot $MySqlConnectorJar
}

if (-not (Test-Path -LiteralPath $connectorJar -PathType Leaf)) {
    throw "MySQL Connector/J was not found at '$connectorJar'. Download the connector JAR and pass its path with -MySqlConnectorJar."
}

$javac = Get-Command javac -ErrorAction SilentlyContinue
$java = Get-Command java -ErrorAction SilentlyContinue
if ($null -eq $javac -or $null -eq $java) {
    throw "Java JDK tools were not found. Install a JDK and make sure java and javac are on PATH."
}

$classesDirectory = Join-Path $projectRoot "build\classes"
New-Item -ItemType Directory -Path $classesDirectory -Force | Out-Null

$sources = Get-ChildItem -Path $projectRoot -Recurse -Filter *.java |
    Where-Object {
        $_.FullName -notmatch '[\\/](\.git|build|out|target)[\\/]'
    } |
    ForEach-Object { $_.FullName }

if ($sources.Count -eq 0) {
    throw "No Java source files were found under '$projectRoot'."
}

& $javac.Source -cp $connectorJar -d $classesDirectory $sources
if ($LASTEXITCODE -ne 0) {
    throw "Java compilation failed with exit code $LASTEXITCODE."
}

$runtimeClasspath = "$classesDirectory;$connectorJar"
$previousUser = $env:GYM_DB_USER
$previousPassword = $env:GYM_DB_PASSWORD
$passwordPointer = [IntPtr]::Zero

try {
    $securePassword = Read-Host "MySQL password for $DatabaseUser" -AsSecureString
    $passwordPointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    $env:GYM_DB_USER = $DatabaseUser
    $env:GYM_DB_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($passwordPointer)

    & $java.Source -cp $runtimeClasspath Main
    if ($LASTEXITCODE -ne 0) {
        throw "The application exited with code $LASTEXITCODE."
    }
} finally {
    if ($passwordPointer -ne [IntPtr]::Zero) {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($passwordPointer)
    }
    $env:GYM_DB_USER = $previousUser
    $env:GYM_DB_PASSWORD = $previousPassword
}
