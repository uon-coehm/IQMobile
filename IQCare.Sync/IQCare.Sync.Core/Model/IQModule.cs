using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IQCare.Sync.Core.Model
{
    // ReSharper disable once InconsistentNaming

  [Table("mst_module")]
  public  class IQModule
    {
        [Key]
        public int ModuleID { get; set; }
        public string ModuleName { get; set; }
        public int DeleteFlag { get; set; }
        public ICollection<IQFeature> Features { get; set; }

      public IQModule()
      {
            Features=new  List<IQFeature>();
      }

      public void AddFeature(IQFeature feature)
      {
          feature.Module = this;
        Features.Add(feature);
      }
        public void AddFeatures(IEnumerable<IQFeature> features)
        {
            foreach (var f in features)
            {
                AddFeature(f);
            }
        }

      public override string ToString()
      {
          return $"{ModuleName} ({ModuleID})";
      }
    }
}

/*
ModuleID	int	Unchecked
ModuleName	varchar(50)	Checked
DeleteFlag	int	Checked
UserId	int	Checked
CreateDate	datetime	Checked
UpdateDate	datetime	Checked
Status	int	Checked
UpdateFlag	int	Checked
Identifier	int	Checked
PharmacyFlag	int	Checked
Unchecked

SELECT        ModuleID, ModuleName, DeleteFlag, UserId, CreateDate, UpdateDate, Status, UpdateFlag, Identifier, PharmacyFlag
FROM            mst_module
*/
