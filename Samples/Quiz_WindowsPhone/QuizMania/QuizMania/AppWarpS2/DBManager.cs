using System;
using System.Collections.Generic;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;

namespace QuizMania.AppWarpS2
{
    public class DBManager
    {
        private static DBManager dbManager;

        internal static String DbFbToken = "FBToken";
        internal static String DB_Profile = "Other_Detail";

        private DBManager()
        {

        }

        public void saveData(String dbName, Object data)
        {
            try
            {
                if (IsolatedStorageSettings.ApplicationSettings.Contains(dbName))
                {
                    IsolatedStorageSettings.ApplicationSettings.Remove(dbName);
                }
                IsolatedStorageSettings.ApplicationSettings.Add(dbName, data);
                IsolatedStorageSettings.ApplicationSettings.Save();
            }
            catch (Exception e)
            { 
            }
        }

        public String getDBData(String dbName)
        {
            String data = IsolatedStorageSettings.ApplicationSettings[dbName].ToString();
            if (data != null && data.Length > 0)
            {
                return data;
            }
            else
            {
                return null;
            }
        }

        public Boolean isDBAvailable(String dbName)
        {
            try
            {
                String data = IsolatedStorageSettings.ApplicationSettings[dbName].ToString();
                return true;
            }
            catch (KeyNotFoundException knfe)
            {
                return false;
            }
        }

        public static DBManager getInstance()
        {
            if (dbManager == null)
            {
                dbManager = new DBManager();
            }
            return dbManager;
        }
        public void cleanData(String dbName)
        {
            IsolatedStorageSettings.ApplicationSettings.Remove(dbName);
            IsolatedStorageSettings.ApplicationSettings.Save();
        }

    }
}
