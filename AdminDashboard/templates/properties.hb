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
            <li><a href="#/props">Properties</a></li>
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
      <h4>Room Properties</h4>
      <div class="well">
        <form class="form-horizontal">
          <div class="control-group">
            <label class="control-label" for="appkey">App Key</label>
            <div class="controls">
              <select name="appkey" id="appkey" class="form-control">
                <option>Loading...</option>                  
              </select>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" for="roomname">Room ID</label>
            <div class="controls">
              <select name="roomname" id="roomname" class="form-control">
                <option>Select AppKey</option>                  
              </select>
              <button type="button" class="btn btn-default btn-lg" name="refresh" id="refresh">
                <span class="icon-refresh"></span> 
              </button>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" for="properties">Room Properties</label>
            <div id="keyValues" data-count="0">
            </div>
            <div class="controls">
              <button type="button" class="btn btn-default btn-lg" name="addProp" id="addProp">
                <span class="icon-plus"></span> 
              </button>
            </div>
            <div class="controls" style="display:none;">
              <textarea name="properties" id="properties" class="form-control" rows="8" ></textarea>
            </div>

          </div>
          <div class="control-group">
            <div class="controls">
              <button type="button" class="btn btn-primary" id="updateRoom">Update Properties</button>
            </div>
          </div>
        </form>
      </div>
      <div class="well" id="log"></div>
      </div>
    </div>
  </div>
</div>