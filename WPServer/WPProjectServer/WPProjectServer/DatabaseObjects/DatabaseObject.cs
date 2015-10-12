using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.DatabaseObjects
{
    public abstract class DatabaseObject
    {
        public virtual Guid Id { get; protected set; }

        public override string ToString()
        {
            return Id.ToString();
        }

        public abstract DatabaseObject GetDetails();

        public abstract bool Save(DatabaseObject dbObject);
    }
}
