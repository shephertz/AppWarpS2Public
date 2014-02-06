<div class="row-fluid">
	<div class="span8 offset2 well"><div style="text-align: center;"><img src="img/logo.png" /></div><h5>Dashboard for your game server</h5></div>
</div>
<div class="row-fluid">
	<div class="span8 offset2 well">
		<form class="form-horizontal" action="#/login" method="post">
			<div class="control-group">
				<label class="control-label" for="username">Username</label>
				<div class="controls">
					<input type="text" name="username" id="username" placeholder="Username" value="admin">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="password">Password</label>
				<div class="controls">
					<input type="password" id="password" name="password" placeholder="Password" value="password">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="host" title="IP address/Hostname of the AppWarp S2 Game Server">Host</label>
				<div class="controls">
					<input type="text" id="host" name="host" placeholder="Host" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="port" title="Port of the AppWarp S2 Game Server">Port</label>
				<div class="controls">
					<input type="text" id="port" name="port" placeholder="Port" value="12346">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn btn-primary">Sign in</button>
				</div>
			</div>
		</form>
	</div>
</div>

<div class="row-fluid">
	<div class="span8 offset2 well" id="loginInfo">Please Sign in to continue</div>
</div>