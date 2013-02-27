<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method="html" omit-xml-declaration="yes" />

	<!-- container -->
	<xsl:template match="fresnel_container">
		<xsl:variable name="tag">
			<xsl:choose>
				<xsl:when test="contains(@style,'display:list;')">ul</xsl:when>
				<!-- <xsl:when test="contains(@style,'display: table;')">table</xsl:when>
				<xsl:when test="contains(@style,'display:table-row;')">tr</xsl:when>
				<xsl:when test="contains(@style,'display: table-row;')">tr</xsl:when>
				<xsl:when test="contains(@style,'display:table-row-group;')">tbody</xsl:when>
				<xsl:when test="contains(@style,'display: table-row-group;')">tbody</xsl:when>
				<xsl:when test="contains(@style,'display:table-cell;')">td</xsl:when>
				<xsl:when test="contains(@style,'display: table-cell;')">td</xsl:when> -->
				<xsl:otherwise>span</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- <xsl:element name="{$tag}">
			<xsl:attribute name="class">
				<xsl:text>fresnel_container</xsl:text>
				<xsl:value-of select="@class" />
			</xsl:attribute>
			<xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute> -->
			<xsl:apply-templates />
		<!-- </xsl:element> -->
	</xsl:template>

	<!-- resource -->
	<xsl:template match="fresnel_resource">
		<xsl:variable name="tag">
			<xsl:choose>
				<xsl:when test="contains(@style,'display:table;')">table</xsl:when>
				<xsl:when test="contains(@style,'display: table;')">table</xsl:when>
				<xsl:when test="contains(@style,'display:table-row;')">tr</xsl:when>
				<xsl:when test="contains(@style,'display: table-row;')">tr</xsl:when>
				<xsl:when test="contains(@style,'display:table-row-group;')">tbody</xsl:when>
				<xsl:when test="contains(@style,'display: table-row-group;')">tbody</xsl:when>
				<xsl:when test="contains(@style,'display:table-cell;')">td</xsl:when>
				<xsl:when test="contains(@style,'display: table-cell;')">td</xsl:when>
				<xsl:when test="contains(@style,'span;')">span</xsl:when>
				<xsl:otherwise>div</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- <xsl:element name="{$tag}">
			<xsl:attribute name="class">
				<xsl:text>fresnel_resource</xsl:text>
				<xsl:value-of select="@class" />
			</xsl:attribute>
			<xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute> -->
			<xsl:apply-templates />
		<!-- </xsl:element> -->
	</xsl:template>

	<!-- label -->
	<xsl:template match="fresnel_label">
		<xsl:element name="span">
			<xsl:attribute name="class">
				<xsl:text>fresnel_label</xsl:text>
				<xsl:value-of select="@class" />
			</xsl:attribute>
			<xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute>
			<xsl:value-of select="." />
		</xsl:element>
	</xsl:template>

	<!-- property -->
	<xsl:template match="fresnel_property">
		<xsl:variable name="tag">
			<xsl:choose>

				<xsl:when test="contains(@class,'relevencemark')">span</xsl:when>
				<xsl:when test="contains(@class,'emrmark')">span</xsl:when>
				<xsl:when test="contains(@class,'qcrmark')">span</xsl:when>
				<xsl:when test="contains(@class,'pcrmark')">span</xsl:when>
				<xsl:when test="contains(@class,'descriptivesubsentence')">div</xsl:when>
				<xsl:when test="contains(@class,'mappingContainsMapping')">ul</xsl:when>
				<xsl:when test="contains(@class,'emHasPatternElement')">span</xsl:when>
				<xsl:when test="contains(@class,'emHasMatching')">span</xsl:when>
				<xsl:when test="contains(@class,'matchingHasKeyword')">span</xsl:when>
				<xsl:when test="contains(@class,'matchingHasResource')">span</xsl:when>
				<xsl:when test="contains(@class,'queryElementHasValue')">span</xsl:when>

				<xsl:when test="contains(@style,'display:table;')">table</xsl:when>
				<xsl:when test="contains(@style,'display: table;')">table</xsl:when>
				<xsl:when test="contains(@style,'display:table-row;')">tr</xsl:when>
				<xsl:when test="contains(@style,'display: table-row;')">tr</xsl:when>
				<xsl:when test="contains(@style,'display:table-row-group;')">tbody</xsl:when>
				<xsl:when test="contains(@style,'display: table-row-group;')">tbody</xsl:when>
				<xsl:when test="contains(@style,'display:table-cell;')">td</xsl:when>
				<xsl:when test="contains(@style,'display: table-cell;')">td</xsl:when>
				<xsl:otherwise>div</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$tag}">
			<xsl:attribute name="class">
				<xsl:text>fresnel_property</xsl:text>
				<xsl:value-of select="@class" />
			</xsl:attribute>
			<xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute>
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>

	<!-- text -->
	<xsl:template match="fresnel_text">
		<xsl:element name="span"><xsl:value-of select="." disable-output-escaping="yes"/></xsl:element>
	</xsl:template>

	<!-- value -->
	<xsl:template match="fresnel_value">
		<xsl:choose>

			<xsl:when test="@type = 'text'">
				<xsl:variable name="title">
					<xsl:choose>
						<xsl:when test="contains(@class,'emrmark')">matching mark</xsl:when>
						<xsl:when test="contains(@class,'qcrmark')">query coverage mark</xsl:when>
						<xsl:when test="contains(@class,'pcrmark')">pattern coverage mark</xsl:when>
						<xsl:otherwise></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="tag">
					<xsl:choose>
						<xsl:when test="contains(@class,'plop')">plop</xsl:when>
						<xsl:otherwise>span</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:element name="{$tag}">
					<xsl:attribute name="class">
						<xsl:text>fresnel_value</xsl:text>
						<xsl:value-of select="@class" />
					</xsl:attribute>
					<xsl:attribute name="title">
						<xsl:value-of select="$title" />
						<!-- <xsl:text>$title</xsl:text> -->
						<!-- <xsl:value-of select="@class" /> -->
					</xsl:attribute>
					<xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute>
					<xsl:value-of select="." />
				</xsl:element>
			</xsl:when>

			<xsl:when test="@type = 'resource'">
				<xsl:variable name="tag">
					<xsl:choose>
						<xsl:when test="contains(@class,'mappingContainsMapping')">li</xsl:when>
						<xsl:when test="contains(@class,'emHasMatching')">span</xsl:when>
						<xsl:when test="contains(@class,'matchingHasResource')">span</xsl:when>
						<xsl:when test="contains(@class,'matchingHasKeyword')">span</xsl:when>
						<xsl:otherwise>div</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:element name="{$tag}">
					<xsl:attribute name="class">
						<xsl:text>fresnel_value</xsl:text>
						<xsl:value-of select="@class" />
					</xsl:attribute>
					<xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute>
					<xsl:apply-templates />
				</xsl:element>
			</xsl:when>

			<xsl:when test="@type = 'a'">
				<xsl:element name="a">
					<xsl:attribute name="class">
						<xsl:text>fresnel_value</xsl:text>
						<xsl:value-of select="@class" />
					</xsl:attribute>
					<xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute>
					<xsl:attribute name="href"><xsl:value-of select="@href" /></xsl:attribute>
					<xsl:value-of select="." />
				</xsl:element>
			</xsl:when>

			<xsl:when test="@type = 'img'">
				<xsl:element name="img">
					<xsl:attribute name="class">
						<xsl:text>fresnel_value</xsl:text>
						<xsl:value-of select="@class" />
					</xsl:attribute>
					<xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="@src" /></xsl:attribute>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>
		
</xsl:stylesheet>