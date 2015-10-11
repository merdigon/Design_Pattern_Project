using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPProjectServer.DatabaseObjects;

namespace WPProjectServer.Logic.SubsystemLogic
{
    public class UserSubLogic : DataPullSubLogic
    {
        public bool ChangeUserPenalty(User user, decimal penaltyToAdd)
        {
            user.Debt += penaltyToAdd;
            return SaveUser(user);
        }

        public bool ChangeUserPenalty(string userCode, decimal penaltyToAdd)
        {
            User user = GetUser(userCode);
            if (user == null)
                throw new Exception("Brak uzytkownika o danym kodzie!");

            user.Debt += penaltyToAdd;
            return SaveUser(user);
        }

        public bool SaveUser(User user)
        {
            return true;
        }

        public bool RegisterUser(User user)
        {
            return true;
        }
    }
}
