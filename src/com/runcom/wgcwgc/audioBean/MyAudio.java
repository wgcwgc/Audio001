package com.runcom.wgcwgc.audioBean;

public class MyAudio
{
	private String id , name , link , source , data , other;

	public String getId()
	{
		return id;
	}

	public void setId(String id )
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name )
	{
		this.name = name;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link )
	{
		this.link = link;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source )
	{
		this.source = source;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data )
	{
		this.data = data;
	}

	public String getOther()
	{
		return other;
	}

	public void setOther(String other )
	{
		this.other = other;
	}

	@Override
	public String toString()
	{
		return "Audio [id=" + id + ", name=" + name + ", link=" + link + ", source=" + source + ", data=" + data + ", other=" + other + "]";
	}

}
