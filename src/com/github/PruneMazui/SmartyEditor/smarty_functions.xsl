<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE smarty [
<!ENTITY nbsp "<xsl:text>&#160;</xsl:text>">
<!ENTITY cr "<xsl:text>&#13;</xsl:text>">
<!ENTITY sp "<xsl:text>&#32;</xsl:text>">
<!ENTITY br "<xsl:text>&lt;br&gt;</xsl:text>">
<!ENTITY bold "<xsl:text>&lt;b&gt;</xsl:text>">
<!ENTITY ebold "<xsl:text>&lt;/b&gt;</xsl:text>">
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" encoding="UTF-8" />

<xsl:template match="/smarty">
<xsl:param name="lang"></xsl:param>
<xsl:for-each select="functions/function">
  <xsl:value-of select="@name"/>
  &cr;
  <xsl:for-each select="parameters/parameter">
    <xsl:if test="@required!='no'">
      <xsl:text>&nbsp;</xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text>=</xsl:text>
    </xsl:if>
  </xsl:for-each>
  &cr;
  <xsl:if test="@space='true'">space</xsl:if>
  &cr;
  <xsl:if test="@block='true'">block</xsl:if>
  &cr;
  <xsl:choose>
    <xsl:when test="desc[@lang=$lang]!=''">
      <xsl:value-of select="desc[@lang=$lang]"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="desc"/>
    </xsl:otherwise>
  </xsl:choose>
  &cr;
  <xsl:for-each select="parameters/parameter">
    &nbsp;&nbsp;&nbsp;&nbsp;

    <xsl:if test="@required='yes'">&bold;</xsl:if>
    <xsl:value-of select="@name"/>
    <xsl:if test="@required='yes'">&ebold;</xsl:if>

    <xsl:if test="@required='no'">
      <xsl:choose>
        <xsl:when test="@default!=''">(Default:<xsl:value-of select="@default"/>)</xsl:when>
        <xsl:otherwise>(optional)</xsl:otherwise>
      </xsl:choose>
    </xsl:if>

    <xsl:text> : </xsl:text>
    <xsl:value-of select="@type"/>
    &nbsp;&nbsp;
    <xsl:choose>
      <xsl:when test="desc[@lang=$lang]!=''">
        <xsl:value-of select="desc[@lang=$lang]"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="desc"/>
      </xsl:otherwise>
    </xsl:choose>
    &br;
  </xsl:for-each>
  &cr;
  <xsl:for-each select="flags/flag">
    &nbsp;&nbsp;&nbsp;&nbsp;
    <xsl:value-of select="@name"/>

    <xsl:text> : </xsl:text>
    <xsl:value-of select="@type"/>
    <xsl:choose>
      <xsl:when test="desc[@lang=$lang]!=''">
        <xsl:value-of select="desc[@lang=$lang]"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="desc"/>
      </xsl:otherwise>
    </xsl:choose>
    &br;
  </xsl:for-each>
  &cr;
</xsl:for-each>
</xsl:template>

</xsl:stylesheet>
