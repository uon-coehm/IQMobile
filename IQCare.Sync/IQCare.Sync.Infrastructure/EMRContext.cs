using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Infrastructure
{
    // ReSharper disable once InconsistentNaming

    public class EMRContext : DbContext
    {
        public EMRContext() : base("name=iqcaredatabase")
        {
            Database.SetInitializer<EMRContext>(null);
        }

        public virtual DbSet<IQModule> Modules { get; set; }
        public virtual DbSet<IQFeature> Features { get; set; }
        public virtual DbSet<IQVisitType> VisitTypes { get; set; }
        public virtual DbSet<IQLocation> Locations { get; set; }
        public virtual DbSet<IQPatient> Patients { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Conventions.Remove<PluralizingTableNameConvention>();
        }
    }
}

/*
            modelBuilder.Entity<MConcept>()
                .HasOptional(i => i.Parent)
                .WithMany(i => i.ChildrenConcepts)
                .HasForeignKey(i => i.ParentId);

            modelBuilder.Entity<Patient>()
                .HasOptional(i => i.Partner)
                .WithMany()
                .HasForeignKey(i => i.PartnerId);
*/


