package com.alibaba.flink.ml.workflow.plugins;

import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;

import com.alibaba.flink.ml.workflow.SchemaProto;
import com.alibaba.flink.ml.workflow.components.examples.ExampleUtils;
import com.alibaba.flink.ml.workflow.components.transformers.BaseTransformer;

public class TestTransformer extends BaseTransformer {
	@Override
	public Table transform(TableEnvironment tableEnvironment, Table table) {
		Table inputTable = getInputTable(0);
		SchemaProto schemaProto = transformerProto.getOutputExampleList(0).getSchema();
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < schemaProto.getNameListCount(); i++){
			stringBuilder.append(schemaProto.getNameList(i)).append(",");
		}
		stringBuilder = stringBuilder.deleteCharAt(stringBuilder.length()-1);
		Table  outputTable = inputTable.select(stringBuilder.toString());
		if(ExampleUtils.isTempTable(transformerProto.getOutputExampleList(0))){
			registerOutputTable(transformerProto.getOutputExampleList(0).getMeta().getName(), outputTable);
		}else {
			outputTable.insertInto(transformerProto.getOutputExampleList(0).getMeta().getName());
		}
		return outputTable;

	}
}