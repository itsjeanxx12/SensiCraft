$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing

$root = Split-Path $PSScriptRoot -Parent
$textureDir = Join-Path $root 'src/main/resources/assets/sensicraft/textures/block/block_sensor'
$modelDir = Join-Path $root 'src/main/resources/assets/sensicraft/models/block/block_sensor'
New-Item -ItemType Directory -Force -Path $textureDir, $modelDir | Out-Null

$colors = @{}
function Color([string]$hex) {
    if (-not $colors.ContainsKey($hex)) { $colors[$hex] = [System.Drawing.ColorTranslator]::FromHtml($hex) }
    return $colors[$hex]
}
function Pixel([int]$x, [int]$y, [string]$hex) {
    if ($x -ge 0 -and $x -lt 16 -and $y -ge 0 -and $y -lt 16) {
        $script:bitmap.SetPixel($script:ox + $x, $script:oy + $y, (Color $hex))
    }
}
function Box([int]$x0, [int]$y0, [int]$x1, [int]$y1, [string]$hex) {
    for ($y = $y0; $y -le $y1; $y++) { for ($x = $x0; $x -le $x1; $x++) { Pixel $x $y $hex } }
}
function BaseFace([int]$seed) {
    for ($y = 0; $y -lt 16; $y++) {
        for ($x = 0; $x -lt 16; $x++) {
            $v = (($x * 17 + $y * 29 + $x * $y * 7 + $seed) % 13)
            $tone = if ($v -lt 2) { '#56616B' } elseif ($v -lt 6) { '#394650' } else { '#2E3A44' }
            Pixel $x $y $tone
        }
    }
    Box 0 0 15 0 '#64727A'; Box 0 15 15 15 '#1B252D'
    Box 0 1 0 14 '#55626B'; Box 15 1 15 14 '#1D2830'
    foreach ($xy in @(@(1,1),@(14,1),@(1,14),@(14,14))) {
        Pixel $xy[0] $xy[1] '#8D754F'
    }
}
function SideFace([bool]$active, [int]$seed) {
    BaseFace $seed
    Box 2 2 13 13 '#1B2A34'
    Box 3 3 12 12 '#17252E'
    Box 2 2 12 2 '#667987'; Box 2 3 2 12 '#536674'
    Box 3 13 13 13 '#17212A'; Box 13 3 13 12 '#101B23'
    Pixel 3 3 '#A48A5C'; Pixel 12 3 '#A48A5C'
    Pixel 3 12 '#8F744D'; Pixel 12 12 '#8F744D'
    $dim = if ($active) { '#188A9B' } else { '#315866' }
    $mid = if ($active) { '#24C8DF' } else { '#4A8795' }
    $bright = if ($active) { '#9BF6F5' } else { '#74AEB7' }
    foreach ($xy in @(@(6,4),@(7,4),@(8,4),@(9,4),@(4,6),@(4,7),@(4,8),@(4,9),@(11,6),@(11,7),@(11,8),@(11,9),@(6,11),@(7,11),@(8,11),@(9,11))) {
        Pixel $xy[0] $xy[1] $dim
    }
    foreach ($xy in @(@(5,5),@(10,5),@(5,10),@(10,10),@(6,6),@(9,6),@(6,9),@(9,9))) {
        Pixel $xy[0] $xy[1] $mid
    }
    Pixel 7 7 $bright; Pixel 8 7 $mid; Pixel 7 8 $mid; Pixel 8 8 $bright
    Pixel 7 3 $bright; Pixel 8 3 $bright
    Pixel 3 7 $mid; Pixel 12 7 $mid
    $red = if ($active) { '#EA6955' } else { '#924D48' }
    $redHi = if ($active) { '#FFB079' } else { '#A46A57' }
    Pixel 6 12 $red; Pixel 7 12 $redHi; Pixel 8 12 $redHi; Pixel 9 12 $red
    Pixel 5 13 '#783C37'; Pixel 10 13 '#783C37'
}
function TopFace([bool]$active) {
    BaseFace 5
    Box 2 2 13 13 '#192B34'; Box 3 3 12 12 '#243640'
    Box 4 4 11 11 '#142A34'
    $dim = if ($active) { '#1B899D' } else { '#3D7382' }
    $hi = if ($active) { '#8AF4F2' } else { '#78ADB6' }
    foreach ($xy in @(@(5,3),@(10,3),@(3,5),@(12,5),@(3,10),@(12,10),@(5,12),@(10,12))) {
        Pixel $xy[0] $xy[1] '#A98657'
    }
    foreach ($xy in @(@(6,4),@(7,4),@(8,4),@(9,4),@(4,6),@(4,7),@(4,8),@(4,9),@(11,6),@(11,7),@(11,8),@(11,9),@(6,11),@(7,11),@(8,11),@(9,11))) {
        Pixel $xy[0] $xy[1] $dim
    }
    Pixel 7 5 $hi; Pixel 8 5 $hi; Pixel 5 7 $hi; Pixel 10 7 $hi
    Pixel 7 10 $hi; Pixel 8 10 $hi
    Box 6 6 9 9 '#173F4A'; Box 7 7 8 8 $hi
    Pixel 7 7 '#DBFFFF'; Pixel 8 8 '#DBFFFF'
}
function BottomFace {
    BaseFace 8
    Box 3 3 12 12 '#293741'; Box 4 4 11 11 '#33434D'
    Box 5 5 10 5 '#5B6A70'; Box 5 10 10 10 '#19252D'
    Pixel 5 8 '#A07951'; Pixel 10 8 '#A07951'
}

$templatePath = Join-Path $PSScriptRoot 'block_sensor_inactive.bbmodel'
foreach ($state in @('inactive', 'active')) {
    $active = $state -eq 'active'
    $script:bitmap = [System.Drawing.Bitmap]::new(64, 64, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    try {
        foreach ($face in @(@(0,0,1),@(16,0,2),@(32,0,3),@(0,16,4))) {
            $script:ox = $face[0]; $script:oy = $face[1]
            SideFace $active $face[2]
        }
        $script:ox = 16; $script:oy = 16; TopFace $active
        $script:ox = 32; $script:oy = 16; BottomFace
        $name = "block_sensor_$state"
        $texturePath = Join-Path $textureDir "$name.png"
        $script:bitmap.Save($texturePath, [System.Drawing.Imaging.ImageFormat]::Png)

        $model = Get-Content $templatePath -Raw | ConvertFrom-Json -AsHashtable
        $model.name = $name
        $model.elements[0].name = 'Block Sensor chassis'
        $cubeId = [guid]::NewGuid().ToString()
        $model.elements[0].uuid = $cubeId
        $model.outliner = @($cubeId)
        $uvs = @{
            north = @(0,0,16,16); east = @(16,0,32,16); south = @(32,0,48,16)
            west = @(0,16,16,32); up = @(16,16,32,32); down = @(32,16,48,32)
        }
        foreach ($faceName in $uvs.Keys) { $model.elements[0].faces[$faceName].uv = $uvs[$faceName] }
        $model.textures[0].name = "$name.png"
        $model.textures[0].relative_path = "$name.png"
        $model.textures[0].uuid = [guid]::NewGuid().ToString()
        $model.textures[0].source = 'data:image/png;base64,' + [Convert]::ToBase64String([IO.File]::ReadAllBytes($texturePath))
        $model.textures[0].layers_enabled = $false
        $model.textures[0].layers = @()
        $model.textures[0].sync_to_project = $null
        $bbPath = Join-Path $PSScriptRoot "$name.bbmodel"
        $model | ConvertTo-Json -Depth 100 -Compress | Set-Content -Path $bbPath -Encoding utf8

        $blockModel = [ordered]@{
            format_version = '1.21.11'; credit = 'Made with Blockbench'
            texture_size = @(64,64)
            textures = [ordered]@{ '0' = "sensicraft:block/block_sensor/$name"; particle = "sensicraft:block/block_sensor/$name" }
            elements = @([ordered]@{
                name = 'Block Sensor chassis'
                from = @(0,0,0); to = @(16,16,16)
                faces = [ordered]@{
                    north = @{ uv = @(0,0,4,4); texture = '#0' }
                    east = @{ uv = @(4,0,8,4); texture = '#0' }
                    south = @{ uv = @(8,0,12,4); texture = '#0' }
                    west = @{ uv = @(0,4,4,8); texture = '#0' }
                    up = @{ uv = @(4,4,8,8); texture = '#0' }
                    down = @{ uv = @(8,4,12,8); texture = '#0' }
                }
            })
        }
        $blockModel | ConvertTo-Json -Depth 15 | Set-Content -Path (Join-Path $modelDir "$name.json") -Encoding utf8
    } finally { $script:bitmap.Dispose() }
}
