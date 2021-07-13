package structures;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class ObjectMinCardinalityStructure extends ConstraintStructure {
   private OWLClassExpression range;
   private int cardinality;

   public ObjectMinCardinalityStructure(OWLClass var1, OWLObjectProperty var2, int var3, OWLClassExpression var4, OWLClassExpression var5, ConstraintAs var6) {
      this.domain = var1;
      this.prop = var2;
      this.cardinality = var3;
      this.range = var4;
      this.constraint = var5;
      this.constrAs = var6;
   }

   public ObjectMinCardinalityStructure(OWLClass var1, OWLClass var2, OWLObjectProperty var3, int var4, OWLClassExpression var5, OWLClassExpression var6, ConstraintAs var7) {
      this.domain = var1;
      this.event = var2;
      this.prop = var3;
      this.cardinality = var4;
      this.range = var5;
      this.constraint = var6;
      this.constrAs = var7;
   }

   public ObjectMinCardinalityStructure() {
   }

   public void setRange(OWLClassExpression var1) {
      this.range = var1;
   }

   public void setProperty(OWLObjectProperty var1) {
      this.prop = var1;
   }

   public void setCardinality(int var1) {
      this.cardinality = var1;
   }

   public OWLClassExpression getRange() {
      return this.range;
   }

   public OWLObjectProperty getProperty() {
      return (OWLObjectProperty)this.prop;
   }

   public int getCardinality() {
      return this.cardinality;
   }

   public ConstraintType getConstraintType() {
      return ConstraintType.OBJECT_MIN_CARDINALITY;
   }
}
