personalization
===============

This is a module that contains personalization components

Installation
------------

Before compiling this module, please download the latest WURFL database from http://wurfl.sourceforge.net and put it
into the src/main/webapp/WEB-INF directory in zipped format. You will then need to adjust the 
src/main/webapp/META-INF/personalization.xml to modify the following property : 

        <property name="wurfl" value="/modules/personalization/WEB-INF/wurfl-2.3.xml.zip"/>

to point to the correct file name.

This file is not part of the module by default because it is distributed under a restrictive license.
