package structures;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;

public class DataSomeValuesFromStructure extends ConstraintStructure {
   private OWLDatatype value;

   public DataSomeValuesFromStructure(OWLClass var1, OWLDataProperty var2, OWLDatatype var3, OWLClassExpression var4, ConstraintAs var5) {
      this.domain = var1;
      this.prop = var2;
      this.value = var3;
      this.constraint = var4;
      this.constrAs = var5;
   }

   public DataSomeValuesFromStructure(OWLClass var1, OWLClass var2, OWLDataProperty var3, OWLDatatype var4, OWLClassExpression var5, ConstraintAs var6) {
      this.domain = var1;
      this.event = var2;
      this.prop = var3;
      this.value = var4;
      this.constraint = var5;
      this.constrAs = var6;
   }

   public DataSomeValuesFromStructure() {
   }

   public void setLiteral(OWLDatatype var1) {
      this.value = var1;
   }

   public void setProperty(OWLDataProperty var1) {
      this.prop = var1;
   }

   public OWLDatatype getRange() {
      return this.value;
   }

   public OWLDataProperty getProperty() {
      return (OWLDataProperty)this.prop;
   }

   public ConstraintType getConstraintType() {
      return ConstraintType.DATA_SOME_VALUES_FROM;
   }
}
