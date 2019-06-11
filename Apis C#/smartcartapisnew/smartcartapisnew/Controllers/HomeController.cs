using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using smartcartapisnew.Models;
using Newtonsoft.Json;

namespace smartcartapisnew.Controllers
{
    public class HomeController : Controller
    {
        public string Index()
        {
            return "LOL";
        }

        //Singup API
        public int signup(string name, string email, string password)
        {
            smartcartdbEntities7 db = new smartcartdbEntities7();
            return db.SIGNUP_USER(name, email, password);

        }

        public class User
        {
            public int id { get; set; }
            public string name { get; set; }
            public string email { get; set; }
            public int role_id { get; set; }
        }

        //Login API
        public string login(string email, string pass)
        {
            smartcartdbEntities7 db = new smartcartdbEntities7();

            if (db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>().Count > 0)
            {
                if (db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].role_id == 1)
                {
                    int id= db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].id;
                    string name = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].name;
                    string email_user = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].email;
                    int role_id = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].role_id;

                    string json = JsonConvert.SerializeObject(new
                    {
                        user = new List<User>()
                    {
                        new User { id = id, name = name, email = email_user, role_id = role_id }
                    }
                    });

                    return json;
                }
                else if (db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].role_id == 2)
                {
                    int id = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].id;
                    string name = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].name;
                    string email_user = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].email;
                    int role_id = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].role_id;

                    string json = JsonConvert.SerializeObject(new
                    {
                        user = new List<User>()
                    {
                        new User { id = id, name = name, email = email_user, role_id = role_id }
                    }
                    });

                    return json;
                }
                else
                {
                    int id = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].id;
                    string name = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].name;
                    string email_user = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].email;
                    int role_id = db.LOGIN_USER(email, pass).ToList<LOGIN_USER_Result>()[0].role_id;

                    string json = JsonConvert.SerializeObject(new
                    {
                        user = new List<User>()
                    {
                        new User { id = id, name = name, email = email_user, role_id = role_id }
                    }
                    });

                    return json;
                }
            }
            else
            {
                return "user not found";
            }

        }

        public class Result
        {
            public int id { get; set; }
            public string name { get; set; }
            public double price { get; set; }
        }

        //Return product on basis of barcode
        public string getProduct(long barcode)
        {
            smartcartdbEntities7 db = new smartcartdbEntities7();
            if (db.RETURN_PRODUCT(barcode).ToList<RETURN_PRODUCT_Result>().Count > 0)
            {
                int ida = db.RETURN_PRODUCT(barcode).ToList<RETURN_PRODUCT_Result>()[0].id;
                string namea = db.RETURN_PRODUCT(barcode).ToList<RETURN_PRODUCT_Result>()[0].name;
                double pricea = db.RETURN_PRODUCT(barcode).ToList<RETURN_PRODUCT_Result>()[0].price.Value;

                string json = JsonConvert.SerializeObject(new
                {
                    product = new List<Result>()
                    {
                        new Result { id = ida, name = namea, price = pricea }
                    }
                });

                return json;
            }
            else
            {
                return "product not found";
            }

        }

        //Create a new receipt into the database on pay
        public decimal? createReceipt(int userid, string date, long total)
        {
            smartcartdbEntities7 db = new smartcartdbEntities7();

            return db.NEW_RECEIPT(userid, date, total).FirstOrDefault();
        }

        public class ResultR
        {
            public int id { get; set; }
            public string date { get; set; }
            public double total { get; set; }
        }

        //Return receipts on basis of userid
        public string getReceipts(int userid)
        {
            smartcartdbEntities7 db = new smartcartdbEntities7();
            List<ResultR> result = new List<ResultR>();

            if (db.GET_RECEIPTS(userid).ToList<GET_RECEIPTS_Result>().Count > 0)
            {
                for(int i = 0; i<db.GET_RECEIPTS(userid).ToList<GET_RECEIPTS_Result>().Count;i++)
                {
                    int ida = db.GET_RECEIPTS(userid).ToList<GET_RECEIPTS_Result>()[i].id;
                    string datea = db.GET_RECEIPTS(userid).ToList<GET_RECEIPTS_Result>()[i].date;
                    double totala = db.GET_RECEIPTS(userid).ToList<GET_RECEIPTS_Result>()[i].total.Value;

                    result.Add(new ResultR { id = ida, date = datea, total = totala });
                        
                }

                var jsonString = JsonConvert.SerializeObject(result);

                return jsonString;
            }
            else
            {
                return "receipts not found";
            }

        }









    }
}
