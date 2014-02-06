<div class="container-fluid">
  <div class="row-fluid">
    <div class="span3 well">
      <!--Sidebar content-->
      <img src="img/logo.png" />
      <h4>AppWarp S2 (alpha)</h4>
      <ul class="nav nav-list">
      	<li><a href="#/dashboard">Home</a></li>
      	<li><a href="#/zones">Zones</a></li>
      	<li><a href="#/rooms">Rooms</a></li>
      </ul><br>
      <a href="index.html" class="btn btn-primary">Sign Out</a>
    </div>
    <div class="span9 well">
      <!--Body content-->
      <h4>Zones</h4>
        <div class="well">
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
      	<div class=" well">
      		<button id="getZones" type="button" class="btn btn-primary">Get Zones</button>
      		<div id="zones"></div>
      	</div>
    </div>
  </div>
</div>