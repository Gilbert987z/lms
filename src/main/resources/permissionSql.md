```sql
INSERT INTO `test`.`sys_permission`(`parent_id`, `name`, `path`, `perms`, `component`,
                                    `type`,  `orderNum`, `status`, `created_at`, `updated_at`)
VALUES (0, '系统管理', '', 'sys:manage', '', 0,  1, 1, now(), now());
INSERT INTO `test`.`sys_permission`(`parent_id`, `name`, `path`, `perms`, `component`,
                                    `type`,  `orderNum`, `status`, `created_at`, `updated_at`)
VALUES (1, '系统管理', '', 'sys:manage', '', 0,  1, 1, now(), now());

```
35

book.borrowLog.list

book.list
book.switch
book.detail
book.create
book.update
book.export

bookPublisher.list
bookPublisher.detail
bookPublisher.save
bookPublisher.update
bookPublisher.switch

bookType.list
bookType.detail
bookType.save
bookType.update
bookType.switch

sys.user.detail
sys.user.list
sys.user.save
sys.user.update
sys.user.role.update
sys.user.delete
sys.user.changStatus

sys.permission.info
sys.permission.list
sys.permission.save
sys.permission.update
sys.permission.delete

sys.role.info
sys.role.list
sys.role.save
sys.role.update
sys.role.delete
sys.role.permission.edit

