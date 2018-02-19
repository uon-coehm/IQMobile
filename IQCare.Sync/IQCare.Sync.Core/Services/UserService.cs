using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Interfaces;
using IQCare.Sync.Core.Interfaces.UoW;
using IQCare.Sync.Core.Model;
using log4net;

namespace IQCare.Sync.Core.Services
{
    public class UserService : IUserService
    {
        private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private IUnitOfWork _uow;

        public UserService(IUnitOfWork uow)
        {
            _uow = uow;
        }

        public IEnumerable<User> GetAll()
        {
            var users = _uow.UserRepository.GetAll();
            //users.ForEach(x => x.Password =string.Empty);
            return users;
        }

        public void Sync(User user)
        {
            return;
            
            if (null == user)
            {
                return;
            }

            var existingUser = _uow.UserRepository.FindBySyncId(user.UuId);
            if (null != existingUser)
            {
                existingUser.UpdateFrom(user);
                _uow.UserRepository.Update(existingUser);
            }

            //TODO: configure Create.User

            else
            {
                _uow.UserRepository.Save(user);
            }
        }

        public void Delete(Guid uuid)
        {
            _uow.UserRepository.Delete(uuid);
        }

        public void SaveChanges()
        {
            _uow.Commit();
        }
    }
}
