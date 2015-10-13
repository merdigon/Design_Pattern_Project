package src.main.java.controllers.SubLogic;

import src.main.java.controllers.LogicFactory;
import src.main.java.models.*;

/**
 * Created by Szymon on 2015-10-13.
 */
public class BookIOSubLogic extends DataPullSubLogic
{
        public BookIOSubLogic(LogicFactory lgf)
        {
                super(lgf);
        }

        public CopyStatus Return(String copyCode) throws Exception {
                Copy copyToReturn = GetCopy(copyCode);
                if (copyToReturn == null)
                        throw new Exception("Egzemplarz o takim numerze nie istnieje w bazie biblioteki!");

                CopyStatus copyStatus = GetCopyStatus(copyToReturn);
                if (copyStatus == null)
                        throw new Exception("Brak operacji do wykonania na egzemplarzu!");

                User returningUser = GetUser(copyStatus.rentBy.code);
                if (returningUser == null)
                        throw new Exception("Brak uzytkownika o danym numerze!");

                if (2==(2/1))//copyStatus.returnTime.CompareTo(DateTime.Now) > 0)
                {
                        //copyStatus.daysDelay = (Date. Now - copyStatus.ReturnTime).TotalDays;
                        logicsFactory.GetUsersLogic().ChangeUserPenalty(returningUser.code, ((double) copyStatus.daysDelay * 0.5));
                }

                DeleteCopyStatus(copyStatus);

                return copyStatus;
        }

        public CopyStatus RentCopy(String copyCode, String userCode) throws Exception {
                Copy copyToRent = GetCopy(copyCode);
                if(copyToRent == null)
                        throw new Exception("Brak egzemplarza o taki numerze!");

                if (!copyToRent.condition.equals(Conditions.Avaliable))
                        throw new Exception("Egzemplarz niedostêpny do wypo¿yczenia!");

                User rentingUser = GetUser(userCode);
                if(rentingUser == null)
                        throw new Exception("Brak uzytkownika o danym numerze!");

                CopyStatus copyToReturn = new CopyStatus();
                copyToReturn.rentTime = null;//DateTime.Now;
                copyToReturn.returnTime = null;//(DateTime.Now).AddMonths(1);
                copyToReturn.rentBy = rentingUser;
                copyToReturn.daysDelay = 0;
                copyToReturn.copy = copyToRent;

                if(InsertNewCopyStatus(copyToReturn))
                        return copyToReturn;
                return null;
        }

        public boolean RegisterNewBook(Book newBook, Copy copyOfNewBook, int QtyOfCopies)
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

        public boolean RegisterNewCopy(Copy copyOfBook)
        {
                if (InsertNewCopy(copyOfBook))
                        return true;
                return false;
        }

        public boolean InsertNewBook(Book newBook)
        {
                return true;
        }

        public boolean InsertNewCopy(Copy newCopy)
        {
                return true;
        }

        public boolean InsertNewCopyStatus(CopyStatus statusToInsert)
        {
                return true;
        }

        public void DeleteCopyStatus(CopyStatus copyStatus)
        {

        }
}
