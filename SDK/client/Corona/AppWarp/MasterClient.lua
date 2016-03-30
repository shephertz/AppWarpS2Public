 require "AppWarp.WarpTypes"
 require "AppWarp.WarpConfigForMasterClient"
 require "AppWarp.WarpUtilities"
 local MasterClient = {}
 local Channel = require "AppWarp.MasterChannel"
 local JSON = require "AppWarp.JSON"
 local RequestBuilder = require "AppWarp.WarpRequestBuilder"
 
 local sessionId = nil;
 local _username
 local lastSendTime = 0
 
 local RequestListenerTable = {}
 local NotificationListenerTable = {}
 local _connectionState = WarpConnectionState.DISCONNECTED;
 
 function MasterClient.initialize(host, port)
   WarpConfigForMasterClient.warp_port = port
   WarpConfigForMasterClient.warp_host = host
   WarpConfigForMasterClient.MasterClient = MasterClient
 end
 
 function MasterClient.addRequestListener(request, listener)
   if(listener == nil) then
     return "bad parameter"
   end   
   RequestListenerTable[request] = listener
 end
 
 function MasterClient.resetRequestListener(request)
   RequestListenerTable[request] = nil
 end
 
 function MasterClient.addNotificationListener(notification, listener)
 if(listener == nil) then
     return "bad parameter"
   end   
   NotificationListenerTable[notification] = listener
 end 
 
 function MasterClient.resetNotificationListener(notification)
   NotificationListenerTable[notification] = nil
 end
 
 function MasterClient.enableTrace(enable)
   WarpConfigForMasterClient.trace = enable
 end
 
 function MasterClient.connect()
    if (_connectionState ~= WarpConnectionState.DISCONNECTED and _connectionState ~= WarpConnectionState.DISCONNECTING) then
		fireConnectionEvent(WarpResponseResultCode.BAD_REQUEST);
		--print("masterclient connection gave a bad request.")
        return;
    else
	_connectionState = WarpConnectionState.CONNECTING;
    end
 end
 
 function fireConnectionEvent(resultCode)   
     if(RequestListenerTable.onConnectDone ~= nil) then
       RequestListenerTable.onConnectDone(resultCode)
     end 
   end
 
   --[[
   function fireDisconnectEvent(resultCode)   
     if(RequestListenerTable.onDisconnectDone ~= nil) then
       RequestListenerTable.onDisconnectDone(resultCode)
     end 
   end
   ]]--
   
 function MasterClient.onConnect(success)
     if(success == true) then
	 _connectionState = WarpConnectionState.CONNECTED;
		fireConnectionEvent(WarpResponseResultCode.SUCCESS);  		
     elseif(_connectionState == WarpConnectionState.DISCONNECTING) then
       _connectionState = WarpConnectionState.DISCONNECTED; 
       if(RequestListenerTable.onDisconnectDone ~= nil) then
         RequestListenerTable.onDisconnectDone(WarpResponseResultCode.SUCCESS)
		 fireConnectionEvent(WarpResponseResultCode.SUCCESS);  
       end
     elseif(_connectionState ~= WarpConnectionState.DISCONNECTED) then
       _connectionState = WarpConnectionState.DISCONNECTED; 
       fireConnectionEvent(WarpResponseResultCode.CONNECTION_ERROR);          
     end
   end
    
 function MasterClient.disconnect() 
        if(_connectionState == WarpConnectionState.DISCONNECTED or _connectionState == WarpConnectionState.DISCONNECTING) then
          if(RequestListenerTable.onDisconnectDone ~= nil) then
            RequestListenerTable.onDisconnectDone(WarpResponseResultCode.BAD_REQUEST)            
            return;
          end          
        end
        sessionId = 0;
		Channel.socket_close();
        _connectionState = WarpConnectionState.DISCONNECTED;
       --fireDisconnectEvent(WarpResponseResultCode.SUCCESS);  
 end
 
 function MasterClient.getAllServers()
    --print('MasterClient.getAllServers')
    if(_connectionState ~= WarpConnectionState.CONNECTED) then             
        return;
    end    
   local serversMsg = RequestBuilder.buildWarpRequest(WarpMessageTypeCode.REQUEST, 0, 0, WarpRequestTypeCode.GET_ALL_SERVERS, 3, WarpContentTypeCode.FLAT_STRING, 0, nil);
    Channel.socket_send(serversMsg);    
  end

 function MasterClient.sendCustomMessage(message)
   -- print('MasterClient.sendCustomMessage')
    if(_connectionState ~= WarpConnectionState.CONNECTED) then             
        return;
    end    
	local chatTable = {}
    chatTable["chat"] = message
    local chatMessage = JSON:encode(chatTable)
    local lengthPayload = string.len(tostring(chatMessage));
	--print("custom message="..chatMessage)
	local customMsg = RequestBuilder.buildWarpRequest(WarpMessageTypeCode.REQUEST,0,0,WarpRequestTypeCode.CLIENT_CUSTOM_MESSAGE,3,WarpContentTypeCode.JSON,lengthPayload, tostring(chatMessage));
	--print("custom message length= "..tostring(#customMsg))
    Channel.socket_send(customMsg);
  end   

  local function onNotify(notifyType, payLoad)
    if((notifyType == WarpNotifyTypeCode.CLIENT_CUSTOM_MESSAGE) and (NotificationListenerTable.onCustomMessageReceived ~= nil)) then
      --NotificationListenerTable.onCustomMessageReceived(WarpResponseResultCode.SUCCESS, payLoadTable['chat'])  
	  NotificationListenerTable.onCustomMessageReceived(WarpResponseResultCode.SUCCESS, payLoad)  
    end           
 end
 
 function MasterClient.Loop()  
     if((Channel.isConnected == false) and (_connectionState == WarpConnectionState.CONNECTING)) then
       Channel.socket_connect()
     end     
     if(Channel.isConnected == true) then
      Channel.socket_recv();
     end    
   end
 
 local function onResponse(requestType, resultCode, payLoad)
   local payLoadTable = {}
   if(resultCode == WarpResponseResultCode.SUCCESS) then
      payLoadTable = JSON:decode(payLoad);  
   end    
   if(requestType == WarpRequestTypeCode.GET_ALL_SERVERS) then 
     if(RequestListenerTable.onGetAllServerDone ~= nil) then
	 MasterServertable = {}
	 local serverTable = {}
    for k,v in pairs (payLoadTable) do
	 local address = {}
	 address.host = v['address']['host']
	 address.port = v['address']['port']
	for i,j in pairs(v['details']) do
	local details = {}
	details.api_key = i
	details.zone_name = j['name']
	table.insert(serverTable,details)
	end
	table.insert(serverTable,address)
     end 
	table.insert(MasterServertable,serverTable)
	end
	--print(JSON:encode(MasterServertable))
      RequestListenerTable.onGetAllServerDone(resultCode,MasterServertable);
   end  
   end
 
 function MasterClient.receivedData(data)
   if(string.len(data)>0) then 
     local parsedLen = 0
     local dataLen = string.len(data)
     while(parsedLen < dataLen) do
       local messageType = string.byte(data, parsedLen+1, parsedLen+1)
       if(messageType == WarpMessageTypeCode.RESPONSE) then
         local requestType, resultCode, payLoad, messageLen  = decodeWarpResponseMessage(partial, parsedLen);
         parsedLen = parsedLen + messageLen
         onResponse(requestType, resultCode, payLoad)
--       elseif(messageType == WarpMessageTypeCode.UPDATE) then
		 else
         local notifyType, payload, messageLen = decodeWarpNotifyMessage(partial, parsedLen)
		 --print("receved payload ="..payload)
         parsedLen = parsedLen + messageLen
         onNotify(notifyType, payload)
       --else
         --assert(false)
       end
     end
   end  
 end  
 
 return MasterClient
 