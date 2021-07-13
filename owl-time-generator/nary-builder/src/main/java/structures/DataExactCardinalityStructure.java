package structures;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;

public class DataExactCardinalityStructure extends ConstraintStructure {
   private OWLDatatype range;
   private int cardinality;

   public DataExactCardinalityStructure(OWLClass var1, OWLDataProperty var2, int var3, OWLDatatype var4, OWLClassExpression var5, ConstraintAs var6) {
      this.domain = var1;
      this.prop = var2;
      this.cardinality = var3;
      this.range = var4;
      this.constraint = var5;
      this.constrAs = var6;
   }

   public DataExactCardinalityStructure(OWLClass var1, OWLClass var2, OWLDataProperty var3, int var4, OWLDatatype var5, OWLClassExpression var6, ConstraintAs var7) {
      this.domain = var1;
      this.event = var2;
      this.prop = var3;
      this.cardinality = var4;
      this.range = var5;
      this.constraint = var6;
      this.constrAs = var7;
   }

   public DataExactCardinalityStructure() {
   }

   public void setRange(OWLDatatype var1) {
      this.range = var1;
   }

   public void setProperty(OWLDataProperty var1) {
      this.prop = var1;
   }

   public void setCardinality(int var1) {
      this.cardinality = var1;
   }

   public OWLDatatype getRange() {
      return this.range;
   }

   public OWLDataProperty getProperty() {
      return (OWLDataProperty)this.prop;
   }

   public int getCardinality() {
      return this.cardinality;
   }

   public ConstraintType getConstraintType() {
      return ConstraintType.DATA_EXACT_CARDINALITY;
   }
}
