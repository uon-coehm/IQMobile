using System.Web.Mvc;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using IQCare.Sync.Wapi;
using IQCare.Sync.Wapi.Controllers;

namespace IQCare.Sync.Wapi.Tests.Controllers
{
    [TestClass]
    public class HomeControllerTest: BaseControllerTest
    {
        [TestMethod]
        public void Index()
        {
            // Arrange
            HomeController controller = new HomeController();

            // Act
            ViewResult result = controller.Index() as ViewResult;

            // Assert
            Assert.IsNotNull(result);
            Assert.AreEqual("Home Page", result.ViewBag.Title);
        }
    }
}
