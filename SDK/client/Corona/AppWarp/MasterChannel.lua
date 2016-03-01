require "socket"
    
local MasterChannel = {}    
local client_socket = nil

MasterChannel.isConnected = false

function MasterChannel.socket_connect()
  if(client_socket == nil) then 
    client_socket = socket.tcp();   
    client_socket:settimeout(0)
  end
  if(MasterChannel.isConnected == true) then
    return
  end
  --print("master host"..WarpConfigForMasterClient.warp_host)
  --print("master port"..WarpConfigForMasterClient.warp_port)
  local status, err = client_socket:connect(WarpConfigForMasterClient.warp_host, WarpConfigForMasterClient.warp_port)
  if(err == "already connected")  then
    --print("channel connected")
    MasterChannel.isConnected = true;
    WarpConfigForMasterClient.MasterClient.onConnect(true);    
  else
    print("error is "..tostring(err));
  end
end
      
function MasterChannel.socket_close()
  client_socket:close();
end

function MasterChannel.socket_send(buffer)
  client_socket:send(buffer)
end
  
function MasterChannel.socket_recv()
  local buffer;
  local status;
  if(client_socket == nil) then
    return
  end 
   
  buffer, status, partial = client_socket:receive('*a');
 
  if(partial ~= nil) then
    local partialstring = tostring(partial);
    local length = string.len(partialstring);
    if(length > 0) then
      --print("read some partial bytes "..tostring(length))
      WarpConfigForMasterClient.MasterClient.receivedData(partial)
    end
  end  
  if (status == "timeout") then
    --print("timeout socket")
  elseif status == "closed" then 
   -- print("closed socket")
    MasterChannel.isConnected = false;    
    client_socket = nil;  	
    WarpConfigForMasterClient.MasterClient.onConnect(false);
  end
end

return MasterChannel    
    
    