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
    [Table("mst_Feature")]
  public  class IQFeature
    {
        [Key]
        public int FeatureID { get; set; }
        public string FeatureName { get; set; }
        public int DeleteFlag { get; set; }
        public int SystemId { get; set; }
        [ForeignKey("Module")]
        public int ModuleId { get; set; }

        public virtual IQModule Module { get; set; }
        public virtual ICollection<IQVisitType> VisitTypes { get; set; }

        public IQFeature()
        {
            VisitTypes=new List<IQVisitType>();
        }

        public void AddVisitType(IQVisitType visitType)
        {
            visitType.Feature = this;
            VisitTypes.Add(visitType);
        }
        public void AddVisitTypes(IEnumerable<IQVisitType> visitTypes)
        {
            foreach (var v in visitTypes)
            {
                AddVisitType(v);
            }
        }
        public override string ToString()
        {
            return $"{FeatureName} ({FeatureID})";
        }

        /*
FeatureID	int	Unchecked
FeatureName	varchar(50)	Checked
ReportFlag	int	Checked
DeleteFlag	int	Checked
AdminFlag	int	Checked
UserID	int	Checked
CreateDate	datetime	Checked
UpdateDate	datetime	Checked
OptionalFlag	int	Checked
SystemId	int	Checked
Published	int	Checked
CountryId	int	Checked
ModuleId	int	Checked
MultiVisit	int	Checked
Seq	int	Checked
RegistrationFormFlag	int	Checked
		Unchecked
		

SELECT        FeatureID, FeatureName, ReportFlag, DeleteFlag, AdminFlag, UserID, CreateDate, UpdateDate, OptionalFlag, SystemId, Published, CountryId, ModuleId, MultiVisit, Seq, RegistrationFormFlag
FROM            mst_Feature
        */
    }
}
