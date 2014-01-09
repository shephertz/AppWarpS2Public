<div class="container-fluid">
  <div class="row-fluid">
    <div class="span2 well">
      <!--Sidebar content-->
      <h3>AppWarp S2 (alpha)</h3>
      <ul class="nav nav-list">
      	<li><a href="#/dashboard">Home</a></li>
      	<li><a href="#/zones">Zones</a></li>
      	<li><a href="#/rooms">Rooms</a></li>
      </ul>
    </div>
    <div class="span10 well">
      <!--Body content-->
      <h4>Zones</h4>
      <div class="row-fluid">
      	<div class="span4 well">
      		<button id="getZones" type="button" class="btn btn-primary">Get Zones</button>
      		<div id="zones"></div>
      	</div>
      	<div class="span8 well">
      		<form class="form-horizontal">
      			<div class="control-group">
					<label class="control-label" for="appname">App Name</label>
					<div class="controls">
						<input type="text" name="appname" id="appname" placeholder="App Name">
						<button type="button" class="btn btn-primary" id="createZone">Create Zone</button>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="appkey">App Key</label>
					<div class="controls">
						<input type="text" name="appkey" id="appkey" placeholder="App Key">
						<button type="button" class="btn btn-primary" id="deleteZone">Delete Zone</button>
					</div>
				</div>
      		</form>

      		<div id="log"></div>
      	</div>
      </div>
    </div>
  </div>
</div>