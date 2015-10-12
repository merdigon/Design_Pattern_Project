using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPProjectServer.DatabaseObjects;

namespace WPProjectServer.Logic.SubsystemLogic
{
    public class BookIOSubLogic : DataPullSubLogic
    {
        public CopyStatus Return(string copyCode)
        {
            Copy copyToReturn = GetCopy(copyCode);
            if (copyToReturn == null)
                throw new Exception("Egzemplarz o takim numerze nie istnieje w bazie biblioteki!");

            CopyStatus copyStatus = GetCopyStatus(copyToReturn);
            if (copyStatus == null)
                throw new Exception("Brak operacji do wykonania na egzemplarzu!");

            User returningUser = GetUser(copyStatus.RentBy.Code);
            if (returningUser == null)
                throw new Exception("Brak uzytkownika o danym numerze!");

            if (copyStatus.ReturnTime.CompareTo(DateTime.Now) > 0)
            {
                copyStatus.DaysDelay = (DateTime.Now - copyStatus.ReturnTime).TotalDays;
                LogicFactory.GetLogic<UserSubLogic>().ChangeUserPenalty(returningUser, (decimal)((double)copyStatus.DaysDelay * 0.5));
            }

            DeleteCopyStatus(copyStatus);

            return copyStatus;
        }

        public CopyStatus RentCopy(string copyCode, string userCode)
        {
            Copy copyToRent = GetCopy(copyCode);
            if(copyToRent == null)
                throw new Exception("Brak egzemplarza o taki numerze!");

            if (!copyToRent.Condition.Equals(Conditions.Avaliable))
                throw new Exception("Egzemplarz niedostępny do wypożyczenia!");

            User rentingUser = GetUser(userCode);  
            if(rentingUser == null)
                throw new Exception("Brak uzytkownika o danym numerze!");

            CopyStatus copyToReturn = new CopyStatus();
            copyToReturn.RentTime = DateTime.Now;
            copyToReturn.ReturnTime = (DateTime.Now).AddMonths(1);
            copyToReturn.RentBy = rentingUser;
            copyToReturn.DaysDelay = 0;
            copyToReturn.Copy = copyToRent;

            if(InsertNewCopyStatus(copyToReturn))
                return copyToReturn;
            return null;
        }

        public bool RegisterNewBook(Book newBook, Copy copyOfNewBook, int QtyOfCopies)
        {
            if(InsertNewBook(newBook))
            {
                for (int a = 0; a < QtyOfCopies; a++)
                    if (!InsertNewCopy(copyOfNewBook))
                        return false;
                return true;
            }
            return false;
        }

        public bool RegisterNewCopy(Copy copyOfBook)
        {
            if (InsertNewCopy(copyOfBook))
                return true;
            return false;
        }

        public bool InsertNewBook(Book newBook)
        {
            return true;
        }

        public bool InsertNewCopy(Copy newCopy)
        {
            return true;
        }
        
        public bool InsertNewCopyStatus(CopyStatus statusToInsert)
        {
            return true;
        }

        public void DeleteCopyStatus(CopyStatus copyStatus)
        {

        }        
    }
}
