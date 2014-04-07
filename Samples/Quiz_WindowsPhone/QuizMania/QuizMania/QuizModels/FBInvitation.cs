using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace QuizMania.QuizModels
{
    public class FBInvitation
    {
        public byte MessageCode { get; set;}
        public string RoomId { get; set; }
        public string HostFBId { get; set; }
        public string RemoteFBId { get; set; }
        public string HostFBName { get; set; }
    }
}
