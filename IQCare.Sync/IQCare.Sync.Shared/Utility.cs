using Sand.Security.Cryptography;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using log4net;
using log4net.Config;
using Newtonsoft.Json;

namespace IQCare.Sync.Shared
{
    public class Utility
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        public static string DbSecurity = "ttwbvXWpqb5WOLfLrBgisw==";

        public static string GetSqlDecrptyion()
        {
            return $"Open symmetric key Key_CTC decryption by password=N'{DbSecurity}';";
        }

        public static string Encrypt(string parameter)
        {
            Encryptor Encry = new Encryptor(EncryptionAlgorithm.TripleDes);
            Encry.IV = Encoding.ASCII.GetBytes("t3ilc0m3");
            return Encry.Encrypt(parameter, "3wmotherwdrtybnio12ewq23");
        }

        public static string Decrypt(string parameter)
        {
            Decryptor Decry = new Decryptor(EncryptionAlgorithm.TripleDes);
            Decry.IV = Encoding.ASCII.GetBytes("t3ilc0m3");
            return Decry.Decrypt(parameter, "3wmotherwdrtybnio12ewq23");
        }

        public static string StoreMessage(object objMessage, string objLocation, string objFile)
        {
            string result;
            objLocation = objLocation.EndsWith(@"\") ? objLocation : $"{objLocation}{@"\"}";
            string msgFile = $"{objLocation}{objFile}.txt";
            try
            {
                Directory.CreateDirectory(objLocation);
            }
            catch (Exception ex)
            {
                
                Log.Debug(ex);
            }
            
            using (StreamWriter file = File.CreateText(msgFile))
            {
                try
                {
                    JsonSerializer serializer = new JsonSerializer();
                    serializer.ReferenceLoopHandling=ReferenceLoopHandling.Ignore;
                    serializer.Serialize(file, objMessage);
                    result = $"{msgFile} | OK";
                }
                catch (Exception ex)
                {
                    result = $"{msgFile} | FAIL";
                    Log.Debug($"Error creating message store {msgFile}");
                    Log.Debug(ex);
                }
            }

            return result;
        }
        
    }
}
