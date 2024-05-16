[33mcommit 2dd83cbe74014e8f966b4d4ee7803f068d8d53be[m[33m ([m[1;36mHEAD -> [m[1;32mmain[m[33m)[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Mon May 13 17:29:42 2024 +0800

    总体优化

[33mcommit 6c33a1aaf997be37fd878d5e9ad02f1ae5505653[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Mon May 13 17:29:22 2024 +0800

    总体优化

[33mcommit d4a06483884dbbc890d5f599c4a8219ca14b6f51[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Mon Apr 29 19:25:01 2024 +0800

    总体优化

[33mcommit 9294eeae95a7a591e1c7c9f14afc869178e9aa11[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Apr 23 17:26:14 2024 +0800

    整体修复和优化

[33mcommit 5e141a50ed2ee0b526bcd1318edbc5a969d10f76[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Apr 23 17:15:40 2024 +0800

    整体修复和优化

[33mcommit 9c8d735225c272b1fba04fed521fa7f00441c188[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Apr 23 17:14:00 2024 +0800

    整体修复和优化

[33mcommit 0b7c2506b1647288c3436204bfc2af01479be610[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Apr 23 17:04:53 2024 +0800

    优化了前端全局 现在能通畅地和云端进行交互

[33mcommit f2faede90f0ba1f2b8bdd42620b5c2d513502cc0[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Fri Apr 19 17:18:59 2024 +0800

    优化了前端的文件结构 将用户卡片拆分为单独的Component

[33mcommit 6db2d6207d893c8c36ba79a610e745bb62362565[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Fri Apr 19 14:18:53 2024 +0800

    完善了任务完成相关的逻辑，现在支持图片上传
    完善了每日任务下发相关的逻辑，现在能正确进行每日任务下发和前日缓存清空了

[33mcommit a775e1b78c6c79f58b1128759ca2d26ddfc19faa[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Apr 16 13:46:55 2024 +0800

    完善了任务撤销相关逻辑 现在能在巡检中途撤销任务了

[33mcommit 047de3ce9ee7a23a20ed2154947b0d0723430a4b[m[33m ([m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Apr 16 12:28:25 2024 +0800

    完善了每日超时判定相关逻辑

[33mcommit fa08f58c8011d0d69840d68957be5596a326f5d8[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Apr 16 10:56:54 2024 +0800

    完善了任务撤销相关的逻辑

[33mcommit 740977cbebcc20ef2db4864aa076609736352d59[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Apr 16 09:38:43 2024 +0800

    添加了前端文件夹 以及其它更改

[33mcommit 4fc34d7738242dc9bfc19b4e42db1d6df77fc7a4[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Apr 16 09:36:55 2024 +0800

    添加了前端文件夹 以及其它更改

[33mcommit 1b9aa99d786dcd67d0ccb2420db45d3c41e3d371[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Thu Apr 4 20:50:59 2024 +0800

    部分重写了Sites的API 现在其更符合REST API的规范了

[33mcommit 3c38d71e2b93e6dc399d436bf08b7780d62251e3[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Wed Apr 3 17:55:58 2024 +0800

    优化了Task的查询 现在能正常携带点位Set了

[33mcommit dd856b48acffdab25f2fa71caa9f68b753b457f6[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Wed Apr 3 17:40:40 2024 +0800

    优化了TaskSiteInstance相关的一系列查询操作 现在使用统一的条件查询入口了

[33mcommit 432c53a0ab97f1f0c49bd3b1b9c71e000ff3c4c0[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Mon Apr 1 20:02:00 2024 +0800

    重构了任务下发、每日任务生成、任务完成、前一日任务超时、漏检自动处理、标记等大量逻辑。增加了缓存层Redis以应对每日点位巡检任务完成情况的临时信息存储、交互。

[33mcommit 7c92cb30146ab49421729937367a853f2173543e[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Fri Mar 22 18:47:35 2024 +0800

    完善了点位的数据结构，增加了与User信息的关联。现在查询点位信息，接口会返回更丰富的数据。

[33mcommit f6818b42188462854cbffb3c634c8767f3540074[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Tue Mar 19 11:45:34 2024 +0800

    为点位增加了巡检频率属性，以JSON格式存储。

[33mcommit 2a429a47ba662905f95ae56c37775c69e78c0820[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Thu Mar 14 15:43:28 2024 +0800

    点位添加稳定性改善，增加失败判定。

[33mcommit 5f0bc213e8a46a76732d5cc75e4d787e18166081[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Thu Feb 22 14:39:27 2024 +0800

    实现了拒绝重复完成点位实例任务的逻辑。

[33mcommit a6835a3b0c7131b49993a051e776f0651fbca8bc[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Thu Feb 22 14:29:21 2024 +0800

    实现了点位实例的巡查表单数据结构的构建，以及提交点位巡查结果的接口和逻辑。

[33mcommit bed6de6d3a50755efcdefaf2b300543a241392a2[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Mon Feb 19 16:05:07 2024 +0800

    修正了系统逻辑，现在可以通过选取点位、日期来创建任务模板类。并通过任务模板类来生成一系列任务实例和任务点位实例。相关逻辑在TaskServiceImpl中实现。
    系统具备了基本的创建任务的能力。但是对于任务点位的具体巡查任务的完成还需要进一步完善。

[33mcommit e3ed2fd0e3cc3ff58333fa744b3011fa749b99cc[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Mon Feb 19 15:59:48 2024 +0800

    修正了系统逻辑，现在可以通过选取点位、日期来创建任务模板类。并通过任务模板类来生成一系列任务实例和任务点位实例。相关逻辑在TaskServiceImpl中实现。
    系统具备了基本的创建任务的能力。但是对于任务点位的具体巡查任务的完成还需要进一步完善。

[33mcommit 8a7565cfe1c1d673b6466c3b788c4c8053ef4105[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Sun Feb 18 17:35:29 2024 +0800

    添加了任务创建的接口、任务实体类。任务的创建是一个复杂的过程，目前仅仅具有雏形，还需要进一步更新。

[33mcommit 420ef54ad7884f0289fe4b371b0973f346bdbe01[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Sat Feb 17 19:15:01 2024 +0800

    添加了点位相关的CRUD操作，本巡检系统为点位驱动，点位实体是本巡检系统的最核心实体类型。

[33mcommit 50151a4deacffa82a752f0aad4497e9b7d55502e[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Wed Feb 14 19:46:02 2024 +0800

    测试3

[33mcommit 49341e4033907e2a9b4f656282b955265c58d9a3[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Wed Feb 14 19:43:06 2024 +0800

    测试2

[33mcommit 9f73e4645124ee4d04892e0574822ca6a541eaa5[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Wed Feb 14 19:38:05 2024 +0800

    测试

[33mcommit 0c43c4dfb6fd2c12433fe3354c47d2723b272da0[m
Author: Super_String <superstringtheory@foxmail.com>
Date:   Wed Feb 14 19:04:22 2024 +0800

    Add files via upload
