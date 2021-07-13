package structures;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class ObjectAllValuesFromStructure extends ConstraintStructure {
   private OWLClassExpression range;

   public ObjectAllValuesFromStructure(OWLClass var1, OWLObjectProperty var2, OWLClassExpression var3, OWLClassExpression var4, ConstraintAs var5) {
      this.domain = var1;
      this.prop = var2;
      this.range = var3;
      this.constraint = var4;
      this.constrAs = var5;
   }

   public ObjectAllValuesFromStructure(OWLClass var1, OWLClass var2, OWLObjectProperty var3, OWLClassExpression var4, OWLClassExpression var5, ConstraintAs var6) {
      this.domain = var1;
      this.event = var2;
      this.prop = var3;
      this.range = var4;
      this.constraint = var5;
      this.constrAs = var6;
   }

   public ObjectAllValuesFromStructure() {
   }

   public void setRange(OWLClassExpression var1) {
      this.range = var1;
   }

   public void setProperty(OWLObjectProperty var1) {
      this.prop = var1;
   }

   public OWLClassExpression getRange() {
      return this.range;
   }

   public OWLObjectProperty getProperty() {
      return (OWLObjectProperty)this.prop;
   }

   public ConstraintType getConstraintType() {
      return ConstraintType.OBJECT_ALL_VALUES_FROM;
   }
}
