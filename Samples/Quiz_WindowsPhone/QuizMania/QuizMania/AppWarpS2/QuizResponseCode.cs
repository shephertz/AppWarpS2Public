using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace QuizMania.AppWarpS2
{
   public static class QuizResponseCode
    {
         public const byte QUESTIONPACKET=0;
         public const byte SCOREPACKET = 1;
         public const byte LEVELSTART=2;
         public const byte LEVELRESULT=3;
         public const byte FINALRESULT=4;
         public const byte FBFRIENDLIST=5;
    }
}
