<div class="row-fluid">
	<div class="span6 offset3 well"><h1>AppWarp S2 (alpha)</h1></div>
</div>
<div class="row-fluid">
	<div class="span6 offset3 well">
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
				<label class="control-label" for="host">Host</label>
				<div class="controls">
					<input type="text" id="host" name="host" placeholder="Host" value="localhost">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="port">Port</label>
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
	<div class="span6 offset3 well" id="loginInfo">Please Sign in to continue</div>
</div>