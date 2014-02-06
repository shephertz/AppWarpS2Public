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
      <div id="dashboardStats">
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
      <div id="dashboardGraph">
        <h4>Memory</h4>
        <canvas id="memoryChart" width="600" height="200"></canvas>
        <h4>CCU</h4>
        <canvas id="ccuChart" width="600" height="200"></canvas>
        <h4>Rooms</h4>
        <canvas id="roomChart" width="600" height="200"></canvas>
      </div>
    </div>
  </div>
</div>