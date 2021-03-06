package org.cmucreatelab.flutter_android.classes.outputs;

import java.io.Serializable;

/**
 * Created by mike on 2/8/17.
 *
 * FlutterOutput
 *
 * An interface for any physical output that appears on a FlutterDevice, which may consist of multiple hardware outputs
 */
public interface FlutterOutput extends Serializable {

    Output[] getOutputs();

}
