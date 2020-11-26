<link rel="stylesheet" href="<?php require_once 'config.php';
								echo URL; ?>/Resources/style/NavigationStyle.css">
<section id="task">
	<!-- bara de sus care e fixed si ai pe ea buton pentru search, add si profile si inventory -->
	<a href="<?php echo URL . "/Resources/coins/search.php" ?>">
		<button class="tabs search search_btn">
			<p>Search</p>
		</button>
	</a>
	<a href="<?php echo URL . "/Resources/coins/add.php" ?>">
		<button class=" tabs add add_btn">
			<p>Add</p>
		</button>
	</a>
	<a href="<?php echo URL . "/Resources/profile/profile.php" ?>">
		<button class="tabs profile profile_btn">
			<p>Profile</p>
		</button>
	</a>
	<a href="<?php echo URL . "/Resources/coins/Inventory.php" ?>">
		<button class="tabs inventory inventory_btn">
			<p>Inventory</p>
		</button>
	</a>
</section>
