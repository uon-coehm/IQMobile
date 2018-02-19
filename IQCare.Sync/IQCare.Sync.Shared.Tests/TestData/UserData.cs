using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Shared.Tests.TestData
{

    public class UserData
    {

        public static List<User> GetTestUsers()
        {
            var users = new List<User>();

            var user = new User();

            user.Username = "TestXUser";
            user.Password = "Zu7BrcApEvdWiVLpjGpuhw==";
            user.CounsellorCode = "1309";
            users.Add(user);

            return users;
        }
        
    }
}
