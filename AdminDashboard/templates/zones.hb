<div class="appwarp_header_wrapper">
  <div class="appwarp_header_inner">
    <div class="appwarp_header">
      <div id="logo"><a title="AppWarp" href="http://appwarp.shephertz.com/" target="_blank"><img border="0" alt="AppWarp" src="img/logo.png"></a></div>
      <div class="top_navi">
        <div class="menu">
          <ul id="dropdown_menu">
            <li><a href="#/dashboard">Home</a> |</li>
            <li><a href="#/zones">Zones</a> |</li>
            <li><a href="#/rooms">Rooms</a></li>
          </ul>
        </div>
      </div>
      <div class="navigation_login"> <a href="index.html" class="signOut">Sign Out</a> </div>
    </div>
  </div>
</div>
<div class="clear"></div>
<div class="body_wrapper">
  <div class="body_inner">
    <div class="body_inner_box"> 
      <!--Body content-->
      <div class="dashboardStats">
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
</div>
