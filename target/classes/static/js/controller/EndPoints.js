//const BASE_URL = "http://localhost:8080/api/";
export const BASE_URL                           = window.location.protocol + "//" + window.location.host;

export const AUTH                               = BASE_URL + "/api/auth";

export const LOG                               = BASE_URL + "/api/logs";
export const LOG_EXPORT                        = BASE_URL + '/api/logs/export';
export const LOG_FILTER                        = BASE_URL + "/api/logs/filter/";

export const AUTHORITY                          = BASE_URL + "/api/authority";

export const ALLOWED_REGISTRATION               = BASE_URL + "/api/allowed_registration";
export const ALLOWED_REGISTRATION_IMPORT        = BASE_URL + "/api/allowed_registration/data_import";
export const ALLOWED_REGISTRATION_TEMPLATE      = BASE_URL + "/api/allowed_registration/download_template";




export const PASSWORD_VERIFICATION_CODE         = BASE_URL + "/api/public/password_reset/verification_code?identity=";
export const RESET_PASSWORD                     = BASE_URL + "/api/public/reset_password";

export const HOME                        = BASE_URL + "/api/home";


export const PERSON                        = BASE_URL + "/api/person";
export const PERSON_TEMPLATE               = BASE_URL + '/api/person/download_template';
export const PERSON_IMPORT                 = BASE_URL + '/api/person/data_import';
export const PERSON_EXPORT                 = BASE_URL + '/api/person/export';
export const PERSON_FILTER                 = BASE_URL + '/api/person/filter';
export const PERSON_SEARCH                 = BASE_URL + '/api/person/search';

export const MUNICIPALITY                       = BASE_URL + "/api/municipality";
export const MUNICIPALITY_TEMPLATE               = BASE_URL + '/api/municipality/download_template';
export const MUNICIPALITY_IMPORT                 = BASE_URL + '/api/municipality/data_import';
export const MUNICIPALITY_EXPORT                 = BASE_URL + '/api/municipality/export';
export const MUNICIPALITY_DELETE                 = BASE_URL + '/api/municipality/{municipalityId}';

export const DISTRICT                       = BASE_URL + "/api/district";
export const DISTRICT_TEMPLATE               = BASE_URL + '/api/district/download_template';
export const DISTRICT_IMPORT                 = BASE_URL + '/api/district/data_import';
export const DISTRICT_EXPORT                 = BASE_URL + '/api/district/export';
export const DISTRICT_DELETE                 = BASE_URL + '/api/district/{districtId}';

export const INVITATIONTYPE                       = BASE_URL + "/api/invitationType";
export const INVITATIONTYPE_TEMPLATE               = BASE_URL + '/api/invitationType/download_template';
export const INVITATIONTYPE_IMPORT                 = BASE_URL + '/api/invitationType/data_import';
export const INVITATIONTYPE_EXPORT                 = BASE_URL + '/api/invitationType/export';
export const INVITATIONTYPE_DELETE                 = BASE_URL + '/api/invitationType/{invitationTypeId}';

export const KLARTEXT                       = BASE_URL + "/api/klartext";
export const KLARTEXT_TEMPLATE               = BASE_URL + '/api/klartext/download_template';
export const KLARTEXT_IMPORT                 = BASE_URL + '/api/klartext/data_import';
export const KLARTEXT_EXPORT                 = BASE_URL + '/api/klartext/export';
export const KLARTEXT_DELETE                 = BASE_URL + '/api/klartext/{invitationTypeId}';

export const LABORATORY                        = BASE_URL + "/api/laboratory";
export const LABORATORY_TEMPLATE               = BASE_URL + '/api/laboratory/download_template';
export const LABORATORY_IMPORT                 = BASE_URL + '/api/laboratory/data_import';
export const LABORATORY_EXPORT                 = BASE_URL + '/api/laboratory/export';

export const COUNTY                        = BASE_URL + "/api/county";
export const COUNTY_TEMPLATE               = BASE_URL + '/api/county/download_template';
export const COUNTY_IMPORT                 = BASE_URL + '/api/county/data_import';
export const COUNTY_EXPORT                 = BASE_URL + '/api/county/export';

export const COUNTYLAB                        = BASE_URL + "/api/county_lab";


export const ROLE                               = BASE_URL + "/api/role";


export const PERSON_SAMPLE                                = BASE_URL + "/api/person_samples";
export const PERSON_SAMPLE_SEARCH                         = BASE_URL + '/api/person_samples/search';
export const PERSON_SAMPLE_FILTER                         = BASE_URL + '/api/person_samples/filter';



export const SAMPLE                        = BASE_URL + "/api/sample";
export const SAMPLE_TEMPLATE               = BASE_URL + '/api/sample/download_template';
export const SAMPLE_IMPORT                 = BASE_URL + '/api/sample/data_import';
export const SAMPLE_EXPORT                 = BASE_URL + '/api/sample/export';


export const PERSON_HPV                         = BASE_URL + "/api/person_hpv";
export const PERSON_HPV_FILTER                  = BASE_URL + '/api/person_hpv/filter';


export const HPV                        = BASE_URL + "/api/hpv";
export const HPV_TEMPLATE               = BASE_URL + '/api/hpv/download_template';
export const HPV_IMPORT                 = BASE_URL + '/api/hpv/data_import';
export const HPV_EXPORT                 = BASE_URL + '/api/hpv/export';

export const PERSON_EXTHPV                         = BASE_URL + "/api/person_extHpv";
export const PERSON_EXTHPV_FILTER                  = BASE_URL + '/api/person_extHpv/filter';


export const EXTHPV                        = BASE_URL + "/api/extHpv";
export const EXTHPV_TEMPLATE               = BASE_URL + '/api/extHpv/download_template';
export const EXTHPV_IMPORT                 = BASE_URL + '/api/extHpv/data_import';
export const EXTHPV_EXPORT                 = BASE_URL + '/api/extHpv/export';


export const PERSON_CELL                = BASE_URL + "/api/person_cell";


export const CELL                        = BASE_URL + "/api/cell";
export const CELL_TEMPLATE               = BASE_URL + '/api/cell/download_template';
export const CELL_IMPORT                 = BASE_URL + '/api/cell/data_import';
export const CELL_EXPORT                 = BASE_URL + '/api/cell/export';

export const PERSON_CELL_FILTER          = BASE_URL + '/api/person_cell/filter';

export const CELL6923                        = BASE_URL + "/api/cell6923";
export const CELL6923_TEMPLATE               = BASE_URL + '/api/cell6923/download_template';
export const CELL6923_IMPORT                 = BASE_URL + '/api/cell6923/data_import';
export const CELL6923_EXPORT                 = BASE_URL + '/api/cell6923/export';


export const USER                               = BASE_URL + "/api/user";
export const USER_BY_ROLE                       = BASE_URL + "/api/user/by_role/";
export const USER_CHANGE_PASSWORD               = BASE_URL + "/api/user/change_password";
export const USER_FIELDS                        = "/api/user/fields?";



export const GET_BIO_BANK_ID                    = BASE_URL+ "/api/public/bio_bank_id";

export const ERROR_MESSAGE                      = BASE_URL + "/api/multi_lang_message";


export const LAB                        = BASE_URL + "/api/lab";
export const LAB_TEMPLATE               = BASE_URL + '/api/lab/download_template';
export const LAB_IMPORT                 = BASE_URL + '/api/lab/data_import';
export const LAB_EXPORT                 = BASE_URL + '/api/lab/export';


export const PARISH                        = BASE_URL + "/api/parish";
export const PARISH_TEMPLATE               = BASE_URL + '/api/parish/download_template';
export const PARISH_IMPORT                 = BASE_URL + '/api/parish/data_import';
export const PARISH_EXPORT                 = BASE_URL + '/api/parish/export';

export const REFERENCETYPE                       = BASE_URL + "/api/referenceType";
export const REFERENCETYPE_TEMPLATE               = BASE_URL + '/api/referenceType/download_template';
export const REFERENCETYPE_IMPORT                 = BASE_URL + '/api/referenceType/data_import';
export const REFERENCETYPE_EXPORT                 = BASE_URL + '/api/referenceType/export';
export const REFERENCETYPE_DELETE                 = BASE_URL + '/api/referenceType/{referenceTypeId}';

