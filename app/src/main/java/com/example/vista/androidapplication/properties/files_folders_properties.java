package com.example.vista.androidapplication.properties;

public class files_folders_properties
{
    private String Id, Name, Fullname, Created_by, Size, Date, Ext, isShared, Type, TypePrivate, Path, Tags, Expiry, Hash, NoOfFiles, NoOfFolders, RowType;
    String s3AccessKey,s3SecretKey,bucketName,filename,fileLoc;
    String message, subject,selectedUserIds,docId,selectedContactIds,expiryDates,rights,password,is_password, ShareId;
    boolean isForward, copy,download,passwordCheck;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCreatedby() {
        return Created_by;
    }

    public void setCreatedby(String Created_by) {
        this.Created_by = Created_by;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String Fullname) {
        this.Fullname = Fullname;
    }

    public String getisShared() {
        return isShared;
    }

    public void setisShared(String isShared) {
        this.isShared = isShared;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String Size) {
        this.Size = Size;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getExt() {
        return Ext;
    }

    public void setExt(String Ext) {
        this.Ext = Ext;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getTypePrivate() {
        return TypePrivate;
    }

    public void setTypePrivate(String TypePrivate) {
        this.TypePrivate = TypePrivate;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String Path) {
        this.Path = Path;
    }

    public String getExpiry() {
        return Expiry;
    }

    public void setExpiry(String Expiry) {
        this.Expiry = Expiry;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String Hash) {
        this.Hash = Hash;
    }

    public String getNoOfFiles() {
        return NoOfFiles;
    }

    public void setNoOfFiles(String NoOfFiles) {
        this.NoOfFiles = NoOfFiles;
    }

    public String getNoOfFolders() {
        return NoOfFolders;
    }

    public void setNoOfFolders(String NoOfFolders) {
        this.NoOfFolders = NoOfFolders;
    }

    public String getRowType() {
        return RowType;
    }

    public void setRowType(String RowType) {
        this.RowType = RowType;
    }



    // Properties for Get Download Details

    public String gets3AccessKey() {
        return s3AccessKey;
    }

    public void sets3AccessKey(String s3AccessKey) {
        this.s3AccessKey = s3AccessKey;
    }

    public String gets3SecretKey() {
        return s3SecretKey;
    }

    public void sets3SecretKey(String s3SecretKey) {
        this.s3SecretKey = s3SecretKey;
    }

    public String getbucketName() {
        return bucketName;
    }

    public void setbucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getfilename() {
        return filename;
    }

    public void setfilename(String filename) {
        this.filename = filename;
    }

    public String getfileLoc() {
        return fileLoc;
    }

    public void setfileLoc(String fileLoc) {
        this.fileLoc = fileLoc;
    }


    // Properties for adding Share Details

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public String getsubject() {
        return subject;
    }

    public void setsubject(String subject) {
        this.subject = subject;
    }

    public String getdocId() {
        return docId;
    }

    public void setdocId(String docId) {
        this.docId = docId;
    }

    public String getselectedUserIds() {
        return selectedUserIds;
    }

    public void setselectedUserIds(String selectedUserIds) {
        this.selectedUserIds = selectedUserIds;
    }

    public String getselectedContactIds() {
        return selectedContactIds;
    }

    public void setselectedContactIds(String selectedContactIds) {
        this.selectedContactIds = selectedContactIds;
    }

    public String getexpiryDates() {
        return expiryDates;
    }

    public void setexpiryDates(String expiryDates) {
        this.expiryDates = expiryDates;
    }

    public String getrights() {
        return rights;
    }

    public void setrights(String rights) {
        this.rights = rights;
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    public String getis_password() {
        return is_password;
    }

    public void setis_password(String is_password) {
        this.is_password = is_password;
    }

    public boolean getisForward() {
        return isForward;
    }

    public void setisForward(boolean isForward) {
        this.isForward = isForward;
    }

    public String getShareId() {
        return ShareId;
    }

    public void setShareId(String ShareId) {
        this.ShareId = ShareId;
    }
//
//    public boolean getdownload() {
//        return download;
//    }
//
//    public void setdownload(boolean download) {
//        this.download = download;
//    }
//
//    public boolean getpasswordCheck() {
//        return passwordCheck;
//    }
//
//    public void setpasswordCheck(boolean passwordCheck) {
//        this.passwordCheck = passwordCheck;
//    }
}



