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
      <h4>Rooms</h4>
      <div class="row-fluid">
        <div class="span6 well">
          <form class="form-horizontal">
            <div class="control-group">
              <label class="control-label" for="appkey">App Key</label>
              <div class="controls">
                <input type="text" name="appkey" id="appkey" placeholder="App Key">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="roomname">Room Name</label>
              <div class="controls">
                <input type="text" name="roomname" id="roomname" placeholder="Room Name">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="maxusers">Max Users</label>
              <div class="controls">
                <input type="text" name="maxusers" id="maxusers" placeholder="Max Users">
              </div>
            </div>
            <div class="control-group">
              <div class="controls">
                <button type="button" class="btn btn-primary" id="createRoom">Create Room</button>
              </div>
            </div>
          </form>
        </div>
        <div class="span6 well">
          <form class="form-horizontal">
            <div class="control-group">
              <label class="control-label" for="appkey2">App Key</label>
              <div class="controls">
                <input type="text" name="appkey2" id="appkey2" placeholder="App Key">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="roomname2">Room ID</label>
              <div class="controls">
                <input type="text" name="roomname2" id="roomname2" placeholder="Room ID">
              </div>
            </div>
            <div class="control-group">
              <div class="controls">
                <button type="button" class="btn btn-primary" id="deleteRoom">Delete Room</button>
              </div>
            </div>
          </form>
        </div>
      </div>
      <div class="row-fluid">
        <div class="span12 well" id="log">

        </div>
      </div>
    </div>
  </div>