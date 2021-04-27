import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import  { Redirect } from 'react-router-dom'
import axios from "axios"
import { Container, Col, Form, FormGroup, Label, Input } from 'reactstrap'
import { useHistory, useParams } from 'react-router-dom'


export default class Environment extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            environmentname: props.environmentname
        };

    }

    componentDidMount() {
    }

    render() {
        const data = this.props.match.params.name;
        return(
            <div>
                <h4>Home &gt; Environment &gt; {data}</h4>
                <p></p>
            </div>
        )
    }








}