<div class="container-fluid">
  <div class="row-fluid">
    <div class="span2 well">
      <!--Sidebar content-->
      <h3>AppWarp S2</h3>
      <ul class="nav nav-list">
      	<li><a href="#/dashboard">Home</a></li>
      	<li><a href="#/zones">Zones</a></li>
      	<li><a href="#/rooms">Rooms</a></li>
      </ul>
    </div>
    <div class="span10 well">
      <!--Body content-->
      <div id="dashboardStats">
        <h4>Stats</h4>
        <table class="table">
          <tr>
            <th>Memory(KB)</th>
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
        </table>
      </div>
      <div id="dashboardGraph">
        <h4>Memory</h4>
        <canvas id="memoryChart" width="700" height="200"></canvas>
        <h4>CCU</h4>
        <canvas id="ccuChart" width="700" height="200"></canvas>
        <h4>Rooms</h4>
        <canvas id="roomChart" width="700" height="200"></canvas>
      </div>
    </div>
  </div>
</div>