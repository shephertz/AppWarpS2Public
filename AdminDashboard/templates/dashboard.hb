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
      <div id="dashboardStats" class="dashboardStats">
        <h4>Stats</h4>
        <table class="table">
          <tr>
            <th>Memory(MB)</th>
            <th>CCU</th>
            <th>Rooms</th>
            <th>Last Update</th>
          </tr>
          <tr>
            <td id="memoryInfo">0</td>
            <td id="ccuInfo">0</td>
            <td id="roomsInfo">0</td>
            <td id="updateInfo">0</td>
          </tr>
          <tr>
            <th colspan="4" align="center">Peak Values</th>
          </tr>
          <tr>
            <td id="memoryPeak">0</td>
            <td id="ccuPeak">0</td>
            <td id="roomsPeak">0</td>
            <td id="updatePeak">0</td>
          </tr>
        </table>
      </div>
      <div id="dashboardGraph" class="dashboardGraph">
        <div class="graph">
		<h4>Memory</h4>
        <canvas id="memoryChart" width="600" height="200"></canvas>
		</div>
        
		<div class="graph mZero">
		<h4>CCU</h4>
        <canvas id="ccuChart" width="600" height="200"></canvas>
		</div>
		<div class="graph">
        <h4>Rooms</h4>
        <canvas id="roomChart" width="600" height="200"></canvas>
		</div>
      </div>
    </div>
  </div>
</div>
