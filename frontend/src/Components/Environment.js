import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import plusImg from '../img/plus.png';
import axios from 'axios';

export default class Environment extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environmentName: this.props.match.params.name,
            environmentId: 1,
            capabilities: 0,
            itApplications: 0,
            programs: 0,
            strategies: 0,
            strategyItems: 0,
            projects: 0,
            resources: 0,
            businessProcesses: 0,
            status: 0,
        };
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`)
            .then(response => {
                this.setState({
                    environmentId: response.data.environmentId,
                    capabilities: response.data.capabilities.length,
                    itApplications: response.data.itApplications,
                    programs: response.data.programs,
                    strategies: response.data.strategies,
                    strategyItems: response.data.strategyItems,
                    projects: response.data.projects,
                    resources: response.data.resources,
                    businessProcesses: response.data.businessProcesses,
                    status: response.data.status
                });
            })
            .catch(error => {
                this.props.history.push('/notfound')
            })
    }

    render() {
        return(
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${this.state.environmentName}`}>{this.state.environmentName}</Link></li>
                    </ol>
                </nav>
                <div className="container jumbotron">
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Capabilities</h5>
                                <div className="text-center">
                                <Link to={`${this.state.environmentName}/capability/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${this.state.environmentName}/capability/all`}><small className="text-muted">{this.state.capabilities} capabilities</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Strategies</h5>
                                <div className="text-center">
                                    <Link to={`${this.state.environmentName}/strategy/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${this.state.environmentName}/strategy/all`}><small className="text-muted">{this.state.strategies} strategies</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Resources</h5>
                                <div className="text-center">
                                    <Link to={`${this.state.environmentName}/resource/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${this.state.environmentName}/resources/all`}><small className="text-muted">{this.state.resources} resources</small></Link>
                            </div>
                        </div>
                    </div>
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">IT-Applications</h5>
                                <div className="text-center">
                                    <Link to={`${this.state.environmentName}/itapplication/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${this.state.environmentName}/itapplication/all`}><small className="text-muted">{this.state.itApplications} IT-applications</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Strategy Items</h5>
                                <div className="text-center">
                                    <Link to={`${this.state.environmentName}/strategyitem/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${this.state.environmentName}/strategyitem/all`}><small className="text-muted">{this.state.strategyItems} stategy items</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Business Processes</h5>
                                <div className="text-center">
                                    <Link to={`${this.state.environmentName}/businessprocess/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${this.state.environmentName}/businessprocess/all`}><small className="text-muted">{this.state.businessProcesses} business processes</small></Link>
                            </div>
                        </div>
                    </div>
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Programs</h5>
                                <div className="text-center">
                                    <Link to={`${this.state.environmentName}/program/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${this.state.environmentName}/programs/all`}><small className="text-muted">{this.state.programs} programs</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Projects</h5>
                                <div className="text-center">
                                    <Link to={`${this.state.environmentName}/project/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${this.state.environmentName}/projects/all`}><small className="text-muted">{this.state.projects} projects</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Status</h5>
                                <div className="text-center">
                                    <Link to={`${this.state.environmentName}/status/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${this.state.environmentName}/status/all`}><small className="text-muted">{this.state.status} statuses</small></Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        )
    }








}