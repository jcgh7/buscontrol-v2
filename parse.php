<?php
$raw = explode("\r\n", file_get_contents("output.txt"));
$out = "";
for($i = 0; $i < count($raw)-10; $i++){
    $out.=$raw[$i].":".$raw[$i+10]."\n";
}
file_put_contents("parsed.txt", $out);