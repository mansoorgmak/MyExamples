@echo off
cd E:\SVN_AppCode\trunk\UMP_FBWEB

::Copy user and shared jsp files to dist folder for marketplace
robocopy jsp\user explode-ear\fbweb\user\jsp /E
robocopy jsp\shared explode-ear\fbweb\user\jsp /E

::Copy buyer and shared jsp files to dist folder for marketplace
robocopy jsp\buyer explode-ear\fbweb\fbweb\jsp /E
robocopy jsp\seller explode-ear\fbweb\fbweb\jsp /E
robocopy jsp\shared explode-ear\fbweb\fbweb\jsp /E
robocopy jsp\admin explode-ear\fbweb\fbweb\jsp /E

::Copy All content of web-inf from buyer for markerplace
robocopy servers\wars\fbweb-war\WEB-INF explode-ear\fbweb\fbweb\WEB-INF
robocopy servers\wars\fbweb-war\WEB-INF\flows explode-ear\fbweb\fbweb\WEB-INF\flows /E
robocopy servers\wars\fbweb-war\WEB-INF\messages explode-ear\fbweb\fbweb\WEB-INF\messages /E
robocopy servers\wars\fbweb-war\WEB-INF\spring explode-ear\fbweb\fbweb\WEB-INF\spring /E
robocopy servers\wars\fbweb-war\WEB-INF\theme explode-ear\fbweb\fbweb\WEB-INF\classes\theme /E
robocopy servers\wars\fbweb-war\WEB-INF\view explode-ear\fbweb\fbweb\WEB-INF\view /E


robocopy servers\wars\seller-war\WEB-INF\flows explode-ear\fbweb\fbweb\WEB-INF\flows /E
robocopy servers\wars\seller-war\WEB-INF\messages explode-ear\fbweb\fbweb\WEB-INF\messages /E
robocopy servers\wars\seller-war\WEB-INF\spring explode-ear\fbweb\fbweb\WEB-INF\spring /E
robocopy servers\wars\seller-war\WEB-INF\theme explode-ear\fbweb\fbweb\WEB-INF\classes\theme /E
robocopy servers\wars\seller-war\WEB-INF\tld explode-ear\fbweb\fbweb\WEB-INF\tld /E	
robocopy servers\wars\seller-war\WEB-INF\view explode-ear\fbweb\fbweb\WEB-INF\view /E	

robocopy servers\wars\buyer-war\WEB-INF\flows explode-ear\fbweb\fbweb\WEB-INF\flows /E
robocopy servers\wars\buyer-war\WEB-INF\messages explode-ear\fbweb\fbweb\WEB-INF\messages /E
robocopy servers\wars\buyer-war\WEB-INF\spring explode-ear\fbweb\fbweb\WEB-INF\spring /E
robocopy servers\wars\buyer-war\WEB-INF\theme explode-ear\fbweb\fbweb\WEB-INF\classes\theme /E
robocopy servers\wars\buyer-war\WEB-INF\tld explode-ear\fbweb\fbweb\WEB-INF\tld /E	
robocopy servers\wars\buyer-war\WEB-INF\view explode-ear\fbweb\fbweb\WEB-INF\view /E


robocopy servers\wars\admin-war\WEB-INF\flows explode-ear\fbweb\fbweb\WEB-INF\flows /E
robocopy servers\wars\admin-war\WEB-INF\messages explode-ear\fbweb\fbweb\WEB-INF\messages /E
robocopy servers\wars\admin-war\WEB-INF\spring explode-ear\fbweb\fbweb\WEB-INF\spring /E
robocopy servers\wars\admin-war\WEB-INF\theme explode-ear\fbweb\fbweb\WEB-INF\classes\theme /E
robocopy servers\wars\admin-war\WEB-INF\tld explode-ear\fbweb\fbweb\WEB-INF\tld /E	
robocopy servers\wars\admin-war\WEB-INF\view explode-ear\fbweb\fbweb\WEB-INF\view /E

robocopy servers\wars\user-war\WEB-INF explode-ear\fbweb\user\WEB-INF /E


::Copy All the js files to marketplace
robocopy js explode-ear\fbweb\fbweb\js /E
robocopy js explode-ear\fbweb\user\js /E


::Copy All the Images files to marketplace
robocopy images explode-ear\fbweb\fbweb\images /E
robocopy images explode-ear\fbweb\user\images /E

::Copy All the Styles files to marketplace
robocopy style explode-ear\fbweb\fbweb\style /E
robocopy style explode-ear\fbweb\user\style /E


::Copy All Bean Class files
robocopy build\com\fedbid\ui\bean explode-ear\fbweb\fbweb\WEB-INF\classes\com\fedbid\ui\bean /E
robocopy build\com\fedbid\ui\bean explode-ear\fbweb\user\WEB-INF\classes\com\fedbid\ui\bean /E

::Copy All buyer Class files
robocopy build\com\fedbid\ui\buyer explode-ear\fbweb\fbweb\WEB-INF\classes\com\fedbid\ui\buyer /E
robocopy build\com\fedbid\ui\buyer explode-ear\fbweb\user\WEB-INF\classes\com\fedbid\ui\buyer /E

::Copy All buyer Class files
robocopy build\com\fedbid\ui\fbweb explode-ear\fbweb\fbweb\WEB-INF\classes\com\fedbid\ui\fbweb /E
robocopy build\com\fedbid\ui\fbweb explode-ear\fbweb\user\WEB-INF\classes\com\fedbid\ui\fbweb /E

::Copy All Proxy Class files
robocopy build\com\fedbid\ui\proxy explode-ear\fbweb\fbweb\WEB-INF\classes\com\fedbid\ui\proxy /E
robocopy build\com\fedbid\ui\proxy explode-ear\fbweb\user\WEB-INF\classes\com\fedbid\ui\proxy /E

::Copy All seller Class files
robocopy build\com\fedbid\ui\seller explode-ear\fbweb\fbweb\WEB-INF\classes\com\fedbid\ui\seller /E
robocopy build\com\fedbid\ui\seller explode-ear\fbweb\user\WEB-INF\classes\com\fedbid\ui\seller /E

::Copy All Shared Class files
robocopy build\com\fedbid\ui\shared explode-ear\fbweb\fbweb\WEB-INF\classes\com\fedbid\ui\shared /E
robocopy build\com\fedbid\ui\shared explode-ear\fbweb\user\WEB-INF\classes\com\fedbid\ui\shared /E

robocopy build\com\fedbid\ui\user  explode-ear\fbweb\user\WEB-INF\classes\com\fedbid\ui\user /E


:: Copy All Admin Class Files
robocopy build\com\fedbid\ui\admin explode-ear\fbweb\fbweb\WEB-INF\classes\com\fedbid\ui\admin /E
robocopy build\com\fedbid\ui\admin explode-ear\fbweb\user\WEB-INF\classes\com\fedbid\ui\admin /E


::Copy Cron Job to Buyer
robocopy build\com\fedbid\cronjob explode-ear\fbweb\fbweb\WEB-INF\classes\com\fedbid\cronjob /E








