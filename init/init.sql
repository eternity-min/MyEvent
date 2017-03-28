drop table googleevent;
create table googleevent (
  id nvarchar(100),
  ical_uid nvarchar(100),
  etag nvarchar(20),
  start datetime not null,
  end datetime not null,
  kind nvarchar(50),
  creator_email nvarchar(4000),
  summary nvarchar(4000),
  description text,
  location nvarchar(4000),
  html_link varchar(4000),
  created datetime not null,
  updated datetime not null,
  primary key (id, ical_uid)
) engine=InnoDB default charset=utf8;

drop table myevent;
create table myevent (
  id nvarchar(100),
  ical_uid nvarchar(100),
  keyword nvarchar(1000),
  category1 nvarchar(1000),
  category2 nvarchar(1000),
  category3 nvarchar(1000),
  requester nvarchar(100),
  content text,
  sr_id nvarchar(20),
  subsystem nvarchar(100),
  created_date datetime not null default now(),
  modified_date datetime not null default now(),
  primary key (id, ical_uid)
) engine=InnoDB default charset=utf8;

drop table category;
create table category (
  id MEDIUMINT not null auto_increment,
  name nvarchar(100) not null,
  level INTEGER not null default 0,
  sort_no integer default 10,
  parent_id MEDIUMINT not null default 0,
  primary key (id)
) engine=InnoDB default charset=utf8;

drop table category_keyword;
create table category_keyword (
  name nvarchar(100) not null UNIQUE,
  category_id MEDIUMINT not null default 0,
  primary key (name)
) engine=InnoDB default charset=utf8;


truncate table category;
INSERT INTO category(id, parent_id, name, level, sort_no) values ('1', '0', 'SR관리', '1', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('2', '0', '운영관리', '1', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('3', '0', '지원업무관리', '1', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('4', '0', '근태관리', '1', '40');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('5', '1', '문의응대', '2', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('6', '1', '간단조회 및 개발', '2', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('7', '1', '일반조회 및 개발', '2', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('8', '2', '연속성 관리', '2', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('9', '2', '성능관리', '2', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('10', '2', '회의', '2', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('11', '2', '사용자 교육', '2', '40');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('12', '3', 'IT 기획', '2', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('13', '3', '프로젝트지원', '2', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('14', '3', '수명업무', '2', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('15', '4', '근태관리', '2', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('16', '5', '단순문의', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('17', '5', '오류문의', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('18', '5', '기타 작업요청', '3', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('19', '6', 'Data 요청', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('20', '6', 'Data 수정', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('21', '6', '프로그램 변경 분석/설계', '3', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('22', '6', '프로그램 변경 개발', '3', '40');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('23', '6', '프로그램 변경 테스트 및 이관', '3', '50');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('24', '6', '프로그램 신규 분석/설계', '3', '60');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('25', '6', '프로그램 신규 개발', '3', '70');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('26', '6', '프로그램 신규 테스트 및 이관', '3', '80');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('27', '6', '환경설정', '3', '90');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('28', '7', 'Data 요청', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('29', '7', 'Data 수정', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('30', '7', '프로그램 변경 분석/설계', '3', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('31', '7', '프로그램 변경 개발', '3', '40');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('32', '7', '프로그램 변경 테스트 및 이관', '3', '50');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('33', '7', '프로그램 신규 분석/설계', '3', '60');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('34', '7', '프로그램 신규 개발', '3', '70');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('35', '7', '프로그램 신규 테스트 및 이관', '3', '80');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('36', '7', '환경설정', '3', '90');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('37', '8', '모니터링', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('38', '8', '장애관리', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('39', '8', '애플리케이션 인수/폐기', '3', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('40', '9', 'S/W 관리', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('41', '9', 'H/W 관리', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('42', '9', '응용 애플리케이션 관리', '3', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('43', '10', '내부 회의', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('44', '10', '외부 회의', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('45', '11', '사용자 교육', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('46', '11', '매뉴얼 작성', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('47', '12', '정보전략 관리', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('48', '12', '품질 관리', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('49', '12', '조직 관리', '3', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('50', '12', '보안 관리', '3', '40');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('51', '13', '분석/설계 지원', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('52', '13', '개발/테스트 지원', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('53', '14', '본사 수명업무', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('54', '14', '고객사 수명업무', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('55', '15', '휴가', '3', '10');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('56', '15', '출장', '3', '20');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('57', '15', '교육', '3', '30');
INSERT INTO category(id, parent_id, name, level, sort_no) values ('58', '15', '공무', '3', '40');

truncate table category_keyword;
INSERT INTO category_keyword(name, category_id) values('문의응대-단순', '16');	INSERT INTO category_keyword(name, category_id) values('단순문의', '16');
INSERT INTO category_keyword(name, category_id) values('문의응대-오류', '17');	INSERT INTO category_keyword(name, category_id) values('오류문의', '17');
INSERT INTO category_keyword(name, category_id) values('문의응대-기타', '18');	INSERT INTO category_keyword(name, category_id) values('기타요청', '18');
INSERT INTO category_keyword(name, category_id) values('간단-Data요청', '19');	INSERT INTO category_keyword(name, category_id) values('Data요청', '19');
INSERT INTO category_keyword(name, category_id) values('간단-Data수정', '20');	INSERT INTO category_keyword(name, category_id) values('Data수정', '20');
INSERT INTO category_keyword(name, category_id) values('간단-변경분석/설계', '21');	INSERT INTO category_keyword(name, category_id) values('변경분석/설계', '21');	INSERT INTO category_keyword(name, category_id) values('분석/설계', '21'); INSERT INTO category_keyword(name, category_id) values('분석설계', '21'); INSERT INTO category_keyword(name, category_id) values('회의', '21');
INSERT INTO category_keyword(name, category_id) values('간단-변경개발', '22');	INSERT INTO category_keyword(name, category_id) values('변경개발', '22');	INSERT INTO category_keyword(name, category_id) values('개발', '22');
INSERT INTO category_keyword(name, category_id) values('간단-변경테스트/이관', '23');	INSERT INTO category_keyword(name, category_id) values('변경테스트/이관', '23');	INSERT INTO category_keyword(name, category_id) values('테스트/이관', '23'); INSERT INTO category_keyword(name, category_id) values('테스트', '23'); INSERT INTO category_keyword(name, category_id) values('이관', '23');
INSERT INTO category_keyword(name, category_id) values('간단-신규분석/설계', '24');	INSERT INTO category_keyword(name, category_id) values('신규분석/설계', '24');
INSERT INTO category_keyword(name, category_id) values('간단-신규개발', '25');	INSERT INTO category_keyword(name, category_id) values('신규개발', '25');
INSERT INTO category_keyword(name, category_id) values('간단-신규테스트/이관', '26');	INSERT INTO category_keyword(name, category_id) values('신규테스트/이관', '26');
INSERT INTO category_keyword(name, category_id) values('간단-환경설정', '27');	INSERT INTO category_keyword(name, category_id) values('환경설정', '27');
INSERT INTO category_keyword(name, category_id) values('일반-Data요청', '28'); INSERT INTO category_keyword(name, category_id) values('일반-데이터조회', '28');
INSERT INTO category_keyword(name, category_id) values('일반-Data수정', '29'); INSERT INTO category_keyword(name, category_id) values('일반-데이터수정', '29'); INSERT INTO category_keyword(name, category_id) values('일반-데이터변경', '29');
INSERT INTO category_keyword(name, category_id) values('일반-변경분석/설계', '30');
INSERT INTO category_keyword(name, category_id) values('일반-변경개발', '31'); INSERT INTO category_keyword(name, category_id) values('일반-변경개발', '31');
INSERT INTO category_keyword(name, category_id) values('일반-변경테스트/이관', '32');
INSERT INTO category_keyword(name, category_id) values('일반-신규분석/설계', '33');
INSERT INTO category_keyword(name, category_id) values('일반-신규개발', '34');
INSERT INTO category_keyword(name, category_id) values('일반-신규테스트/이관', '35');
INSERT INTO category_keyword(name, category_id) values('일반-환경설정', '36');
INSERT INTO category_keyword(name, category_id) values('모니터링', '37');
INSERT INTO category_keyword(name, category_id) values('장애관리', '38');
INSERT INTO category_keyword(name, category_id) values('애플리케이션인수/폐기', '39');
INSERT INTO category_keyword(name, category_id) values('S/W관리', '40');
INSERT INTO category_keyword(name, category_id) values('H/W관리', '41');
INSERT INTO category_keyword(name, category_id) values('응용애플리케이션관리', '42');
INSERT INTO category_keyword(name, category_id) values('내부회의', '43');
INSERT INTO category_keyword(name, category_id) values('외부회의', '44');
INSERT INTO category_keyword(name, category_id) values('사용자교육', '45');
INSERT INTO category_keyword(name, category_id) values('매뉴얼작성', '46');
INSERT INTO category_keyword(name, category_id) values('정보전략관리', '47');
INSERT INTO category_keyword(name, category_id) values('품질관리', '48');
INSERT INTO category_keyword(name, category_id) values('조직관리', '49');
INSERT INTO category_keyword(name, category_id) values('보안관리', '50');
INSERT INTO category_keyword(name, category_id) values('분석/설계지원', '51');
INSERT INTO category_keyword(name, category_id) values('개발/테스트지원', '52');
INSERT INTO category_keyword(name, category_id) values('본사수명업무', '53');
INSERT INTO category_keyword(name, category_id) values('고객사수명업무', '54');
INSERT INTO category_keyword(name, category_id) values('휴가', '55');	INSERT INTO category_keyword(name, category_id) values('연차', '55');	INSERT INTO category_keyword(name, category_id) values('반차', '55');
INSERT INTO category_keyword(name, category_id) values('출장', '56');
INSERT INTO category_keyword(name, category_id) values('교육', '57');
INSERT INTO category_keyword(name, category_id) values('공무', '58');
INSERT INTO category_keyword(name, category_id) values('ITRM', '37');
INSERT INTO category_keyword(name, category_id) values('업무회의', '16');
INSERT INTO category_keyword(name, category_id) values('문의응대', '16');
INSERT INTO category_keyword(name, category_id) values('데이터요청', '19');	INSERT INTO category_keyword(name, category_id) values('데이터조회', '19');
INSERT INTO category_keyword(name, category_id) values('데이터수정', '20');
INSERT INTO category_keyword(name, category_id) values('일반-분석/설계', '30');	INSERT INTO category_keyword(name, category_id) values('일반-분석설계', '30');
INSERT INTO category_keyword(name, category_id) values('일반-개발', '31');
INSERT INTO category_keyword(name, category_id) values('일반-테스트/이관', '32'); INSERT INTO category_keyword(name, category_id) values('일반-테스트', '32'); INSERT INTO category_keyword(name, category_id) values('일반-이관', '32');
INSERT INTO category_keyword(name, category_id) values('당직근무', '37');