package com.gqm3242.utils;

import com.gqm3242.model.ValidationOp;
import com.gqm3242.validation.ComparisonType;
import com.gqm3242.validation.ValidationCardinality;

public interface ValidationPhase4Int {

	boolean validate(ValidationOp validationOp);

	boolean internalConsistency(ValidationCardinality cardinality, ComparisonType compType, Boolean userDefined,
                                String measureTaskId);

	boolean plausibility(ValidationCardinality cardinality, ComparisonType compType, Boolean userDefined,
                         String measureTaskId);

	boolean abnormalData(ValidationCardinality cardinality, ComparisonType compType, Boolean userDefined,
                         String measureTaskId);

	boolean externalConsistency(ValidationCardinality cardinality, ComparisonType compType, Boolean userDefined,
                                String measureTaskId);

	boolean one2One(String measureTaskId, ComparisonType compType, boolean userDefined);

	boolean one2Many(String measureTaskId, ComparisonType compType, boolean userDefined);

	boolean many2One(String measureTaskId, ComparisonType compType, boolean userDefined);

	boolean many2Many(String measureTaskId, ComparisonType compType, boolean userDefined);

	boolean compare(ComparisonType compType, int data, boolean userDefined);

	boolean compare(ComparisonType compType, double data, boolean userDefined);

}
