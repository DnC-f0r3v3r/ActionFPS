<?php
require_once("../render.inc.php");
require("../render_game.inc.php");

$clanwar = json_decode(file_get_contents('http://woop.ac:81/ActionFPS-PHP-Iterator/api/clanwar.php?id=' . rawurlencode($_GET['id'])), true);
?>
<div id="game">
<?php render_war($clanwar, true); ?>

<?php foreach($clanwar['games'] as $game) { ?>
    <?php render_game($game, true)); ?>
<?php } ?>
</div><?php
echo $foot;