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
      <h4>Rooms</h4>
      <div class="row-fluid">
        <div class="span6 well">
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
                <select name="appkey2" id="appkey2" class="form-control">
                  <option>Loading...</option>                  
                </select>
              </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="roomname2">Room ID</label>
              <div class="controls">
                <select name="roomname2" id="roomname2" class="form-control">
                  <option>Select AppKey</option>                  
                </select>
                <button type="button" class="btn btn-default btn-lg" name="refresh" id="refresh">
                  <span class="icon-refresh"></span> 
                </button>
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
        <div class="span6 well" id="log">

        </div>
        <div class="span6 well">
          <form class="form-horizontal">
            <div class="control-group">
              <label class="control-label" for="appkey3">App Key</label>
              <div class="controls">
                <select name="appkey3" id="appkey3" class="form-control">
                  <option>Loading...</option>                  
                </select>
              </div>
            </div>
            <div class="control-group">
              <div class="controls">
                <button type="button" class="btn btn-primary" id="getRooms">Get Rooms</button>
              </div>
            </div>
          </form>
          <div id="rooms">
          </div>
        </div>
      </div>
    </div>
  </div>