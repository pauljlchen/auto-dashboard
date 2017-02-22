package org.xbot.core.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Created by paulc on 2/17/2017.
 */
public class GenericController {
    final public String Project_View = "project";
    final public String Test_View = "test";
    final public String Record_View ="record";
    final public String Dashboard_View ="dashboard";

    final public String Dashboard_Controller ="dashboards";

    protected Logger log = LogManager.getLogger("dashboard");

    String MESSAGE="MSG";
    String RESULT="RESULT";
}
