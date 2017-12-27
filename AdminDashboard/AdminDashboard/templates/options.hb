<div class="appwarp_header_wrapper">
  <div class="appwarp_header_inner">
    <div class="appwarp_header">
      <div id="logo"><a title="AppWarp" href="http://appwarp.shephertz.com/" target="_blank"><img border="0" alt="AppWarp" src="img/logo.png"></a></div>
      <div class="top_navi">
        <div class="menu">
          <ul id="dropdown_menu">
            <li><a href="#/dashboard">Home</a> |</li>
            <li><a href="#/zones">Zones</a> |</li>
            <li><a href="#/rooms">Rooms</a> |</li>
            <li><a href="#/props">Properties</a> |</li>
			<li><a href="#/options">Options</a></li>
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
      <h4>Options</h4>
      <div class="well">
        <form class="form-horizontal">
          <div class="control-group">
            <label id="optionsLable" class="control-label" for="license">License</label>
            <div class="controls" id="optionsControls">
              <input type="text" name="license" id="license" placeholder="License Key">
              <button type="button" class="btn btn-primary" id="activate">Activate</button>
              <button type="button" class="btn btn-primary" id="deactivate">Release</button>
			  <!-- <br/><br/><label><input type="checkbox"> Send Statistics to Shephertz</label>	-->
            </div>
          </div>
        </form>
        <div id="log" class="text-info">Saving</div>
      </div>
	  <div class="well" id="licenseInfo">
        <h4>Current License</h4>
		<p>CCUs : <br>Validity : </p>
      </div>
	  <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"> Close</span></button>
			<h4 class="modal-title" id="myModalLabel">Other Licenses</h4>
		  </div>
		  <div class="modal-body">
			Unlink your other license key being used on different server.<br><br>
			<form class="form-horizontal">
			  <div class="control-group">
				<label id="optionsLable" class="control-label" for="unlink_license">License</label>
				<div class="controls" id="optionsControls">
				  <input type="text" name="unlink_license" id="unlink_license" placeholder="License Key">
				  <button type="button" class="btn btn-primary" id="unlink">Unlink</button>
				</div>
			  </div>
			</form>
			<div id="unlink_log" class="text-info"></div>
		  </div>
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		  </div>
		</div>
	  </div>
	</div>
	  <div class="well">
		<button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">Other Settings</button>
      </div>
      </div>
    </div>
  </div>
</div>
