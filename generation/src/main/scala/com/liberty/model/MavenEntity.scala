package com.liberty.model

import scala.beans.BeanProperty

/**
 * User: mkontarev
 * Date: 10/28/13
 * Time: 5:00 PM
 */
class MavenEntity(@BeanProperty var projectName: String = "", var groupId: String = "", var path: String = "") {}
