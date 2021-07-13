package structures;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;

public class ConstraintStructure {
   OWLClass domain;
   OWLClass event;
   OWLEntity prop;
   OWLClassExpression constraint;
   ConstraintAs constrAs;

   public ConstraintStructure() {
   }

   public ConstraintStructure(OWLClass var1, OWLClassExpression var2, ConstraintAs var3) {
      this.domain = var1;
      this.constraint = var2;
      this.constrAs = var3;
   }

   public void setDomain(OWLClass var1) {
      this.domain = var1;
   }

   public void setConstrainAs(ConstraintAs var1) {
      this.constrAs = var1;
   }

   public void setConstraint(OWLClassExpression var1) {
      this.constraint = var1;
   }

   public void setProperty(OWLEntity var1) {
      this.prop = var1;
   }

   public void setEventClass(OWLClass var1) {
      this.event = var1;
   }

   public OWLClass getDomain() {
      return this.domain;
   }

   public ConstraintAs getConstraintAs() {
      return this.constrAs;
   }

   public OWLClassExpression getConstraint() {
      return this.constraint;
   }

   public OWLEntity getProperty() {
      return this.prop;
   }

   public OWLClass getEventClass() {
      return this.event;
   }

   public ConstraintType getConstraintType() {
      return ConstraintType.NOT_CONSTRAINT;
   }

   public boolean isTemporal() {
      return this.event != null;
   }

   public static enum ConstraintType {
      NOT_CONSTRAINT,
      OBJECT_ALL_VALUES_FROM,
      OBJECT_SOME_VALUES_FROM,
      DATA_ALL_VALUES_FROM,
      DATA_SOME_VALUES_FROM,
      OBJECT_MAX_CARDINALITY,
      DATA_MAX_CARDINALITY,
      OBJECT_MIN_CARDINALITY,
      OBJECT_EXACT_CARDINALITY,
      DATA_EXACT_CARDINALITY,
      DATA_MIN_CARDINALITY,
      OBJECT_HAS_VALUE,
      DATA_HAS_VALUE;
   }

   public static enum ConstraintAs {
      SUPER,
      EQUIV;
   }
}
